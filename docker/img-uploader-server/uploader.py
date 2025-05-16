from flask import Flask, request, jsonify
import os

UPLOAD_FOLDER = '/uploads'
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

@app.route('/upload', methods=['POST'])
def upload_image():
    if 'image' not in request.files:
        return jsonify({'error': 'El multipart file debe llegar en una propiedad: image'}), 404

    file = request.files['image']
    if file.filename == '':
        return jsonify({'error': 'No hay un archivo seleccionado'}), 404

    path = os.path.join(app.config['UPLOAD_FOLDER'], file.filename)
    file.save(path)
    return jsonify({'url': f'http://localhost:9090/{file.filename}'}), 201


@app.route('/delete', methods=['DELETE'])
def delete_image():
    filename = request.args.get('filename')

    if not filename:
        return jsonify({'error': 'El parámetro "filename" es requerido'}), 400

    file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)

    try:
        os.remove(file_path)
        return jsonify({'message': f'Archivo "{filename}" eliminado correctamente'}), 200
    except Exception as e:
        return jsonify({'error': f'Error al eliminar archivo: {str(e)}'}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)