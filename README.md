This is a custom AI camera that can detect objects live using your computer’s webcam or a Raspberry Pi camera. It’s easy to set up, runs fast, and works with models like YOLOv5 or your own custom-trained .pt files.

It can:

- Real-time object detection from a camera feed

- Works with PyTorch .pt models (YOLOv5, YOLOv8, or custom-trained)

- Draws bounding boxes and class labels on screen

- Supports webcams, USB cameras, IP cameras, and Pi cameras

example:

<img width="554" height="730" alt="image" src="https://github.com/user-attachments/assets/a2d6ea2d-54d1-44c0-8684-c816fb1ce0cd" />


Setup Instructions for running on computer:

Clone the Repo: git clone https://github.com/ishaan-ahuja/ObjectDetectionFTC.git

Feel free to use it now with any python project you want.

Running on Pi:

If you want to run the AI camera on a Raspberry Pi (such as for a portable detection project), make sure your Raspberry Pi is properly set up with Python 3.11 and the camera interface enabled. First, use the command sudo raspi-config to turn on the Pi Camera, or plug in a USB camera. Once that’s done, install Python packages using pip3 install -r requirements.txt. After installation, place your trained .pt model file in the models/ folder and adjust the config.yaml file to match your setup. Finally, run the detection pipeline with python3 camera.py. 
