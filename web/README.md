# Browser Demo (EC)

This folder is the browser-side scaffold for EC demonstration.

## Expected files
- `index.html`
- `run.js`
- one or more generated `.wasm` files (copy them into this folder)

## Run locally
Use any static file server from project root, for example:

```powershell
# Python
python -m http.server 8080
```

Open:

```text
http://localhost:8080/web/
```

Then set the wasm filename (for example `loop_control.wasm`) and click `Run WASM`.
