const logEl = document.getElementById('log');
const runBtn = document.getElementById('runBtn');
const wasmPathInput = document.getElementById('wasmPath');

function log(msg) {
  logEl.textContent += msg + '\n';
  logEl.scrollTop = logEl.scrollHeight;
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

  const importObject = {
    env: {
      memory: new WebAssembly.Memory({ initial: 4 }),
      table: new WebAssembly.Table({ initial: 0, element: 'anyfunc' })
    }
  };

  const bytes = await response.arrayBuffer();
  const { instance } = await WebAssembly.instantiate(bytes, importObject);

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
