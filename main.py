from flask import Flask, render_template, request, jsonify
import cv2
import numpy as np
import base64
from io import BytesIO
from PIL import Image

app = Flask(__name__)

# HSV bounds
COLOR_RANGES = {
    'blue': ([100, 150, 50], [140, 255, 255]),
    'green': ([40, 70, 50], [80, 255, 255]),
    'red': ([0, 100, 50], [10, 255, 255])
}


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/process', methods=['POST'])
def process():
    data = request.get_json()
    color = data['color']
    image_data = data['image'].split(',')[1]  # base64 data

    # Decode image
    img_bytes = base64.b64decode(image_data)
    img = Image.open(BytesIO(img_bytes))
    frame = cv2.cvtColor(np.array(img), cv2.COLOR_RGB2BGR)

    # HSV mask
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    lower, upper = COLOR_RANGES[color]
    mask = cv2.inRange(hsv, np.array(lower), np.array(upper))
    result = cv2.bitwise_and(frame, frame, mask=mask)

    # Encode result to base64
    _, buffer = cv2.imencode('.jpg', result)
    result_base64 = base64.b64encode(buffer).decode('utf-8')

    return jsonify({'image': 'data:image/jpeg;base64,' + result_base64})


app.run(host='0.0.0.0', port=81)
