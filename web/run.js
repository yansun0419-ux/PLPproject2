const logEl = document.getElementById('log');
const runBtn = document.getElementById('runBtn');
const wasmPathInput = document.getElementById('wasmPath');

let memoryRef = null;

function log(msg) {
  logEl.textContent += msg + '\n';
  logEl.scrollTop = logEl.scrollHeight;
}

function readCString(memory, ptr) {
  if (!memory || ptr === 0) {
    return '';
  }

  const bytes = new Uint8Array(memory.buffer);
  let result = '';
  for (let index = ptr; index < bytes.length; index++) {
    const byte = bytes[index];
    if (byte === 0) {
      break;
    }
    result += String.fromCharCode(byte);
  }
  return result;
}

function formatPrintf(memory, formatPtr, values) {
  const format = readCString(memory, formatPtr);
  let valueIndex = 0;
  let output = '';

  for (let i = 0; i < format.length; i++) {
    const ch = format[i];
    if (ch !== '%') {
      if (ch === '\\' && format[i + 1] === 'n') {
        output += '\n';
        i++;
        continue;
      }
      output += ch;
      continue;
    }

    const spec = format[++i];
    const nextValue = values[valueIndex++];
    if (spec === 'd') {
      output += String(nextValue | 0);
    } else if (spec === 's') {
      output += readCString(memory, nextValue >>> 0);
    } else if (spec === '%') {
      output += '%';
    } else {
      output += '%' + spec;
    }
  }

  return output;
}

async function runWasm() {
  logEl.textContent = '';
  const wasmPath = wasmPathInput.value.trim();
  if (!wasmPath) {
    log('Please provide a wasm file path.');
    return;
  }

  log('Loading ' + wasmPath + ' ...');

  const response = await fetch(wasmPath);
  if (!response.ok) {
    throw new Error('Cannot load wasm file: ' + response.status + ' ' + response.statusText);
  }

  const bytes = await response.arrayBuffer();
  const module = await WebAssembly.compile(bytes);
  const imports = WebAssembly.Module.imports(module);

  let instance = null;
  const importObject = {
    env: {
      printf: (formatPtr, ...values) => {
        const memory = memoryRef || instance?.exports?.memory || null;
        const text = formatPrintf(memory, formatPtr, values);
        if (text.length > 0) {
          log(text.replace(/\n$/, ''));
        } else {
          log('');
        }
        return 0;
      },
      scanf: (formatPtr, outPtr) => {
        const memory = memoryRef || instance?.exports?.memory || null;
        const format = readCString(memory, formatPtr);
        if (!memory || !format.includes('%d')) {
          return 0;
        }

        const raw = window.prompt('Enter an integer:', '0');
        const parsed = Number.parseInt(raw ?? '0', 10);
        new DataView(memory.buffer).setInt32(outPtr >>> 0, Number.isFinite(parsed) ? parsed : 0, true);
        return 1;
      }
    }
  };

  if (imports.some((entry) => entry.kind === 'memory')) {
    importObject.env.memory = new WebAssembly.Memory({ initial: 4 });
  }

  if (imports.some((entry) => entry.kind === 'table')) {
    importObject.env.table = new WebAssembly.Table({ initial: 0, element: 'anyfunc' });
  }

  instance = await WebAssembly.instantiate(module, importObject).then((result) => result.instance);
  memoryRef = instance.exports.memory || importObject.env.memory || null;

  log('WASM exports:');
  for (const key of Object.keys(instance.exports)) {
    log(' - ' + key);
  }

  if (typeof instance.exports._start === 'function') {
    log('Running _start()');
    instance.exports._start();
    log('Done.');
    return;
  }

  if (typeof instance.exports.main === 'function') {
    log('Running main()');
    const rc = instance.exports.main();
    log('main() returned: ' + rc);
    return;
  }

  log('No _start/main export found.');
}

runBtn.addEventListener('click', () => {
  runWasm().catch((err) => log('[ERROR] ' + err.message));
});
