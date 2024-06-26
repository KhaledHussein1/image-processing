# Image Processing Application

This Java application, built using Swing for the GUI, is designed for basic image manipulation operations. It allows users to open, process, and save images through an intuitive graphical interface. The core functionalities include image processing operations such as copying, graying out, smoothing (using Cone and Gaussian filters), sharpening, edge detection (using LoG and Marr filters), and rescaling. These operations are implemented using various kernel operations on the images.

## Features

- **File Operations:** Users can open an image from their file system, save the processed image, and exit the application.
- **Image Processing Operations:**
  - **Copy:** Duplicates the original image.
  - **Gray Scale:** Converts the image to grayscale.
  - **Smoothing:** Applies smoothing filters (Cone and Gaussian) to reduce noise.
  - **Sharpen:** Enhances the edges within the image.
  - **Edge Detection:** Utilizes Laplacian of Gaussian (LoG) and Marr-Hildreth algorithms for edge detection.
  - **Rescale:** Adjusts the brightness and contrast of the image.

## Implementation Details

The application is structured around a `JFrame` window containing two panels: one for the original image and another for the processed image. A menu bar offers options for file operations and the different processing techniques. Action listeners are used to handle user interactions, invoking the appropriate methods for each operation.

Image processing is achieved through the use of `BufferedImageOp` implementations, including convolution operations with specific kernels for smoothing, sharpening, and edge detection, as well as color conversion for grayscale and rescale operations.

The GUI also features an image panel class for displaying images along with titles indicating the operation performed. This class extends `JPanel` and overrides the `paint` method to draw the image and titles accordingly.

## Usage

To use the application, simply run it to bring up the GUI. From there, you can open an image file to begin processing. After choosing an operation from the "Process" menu, the processed image will be displayed alongside the original. Processed images can be saved back to the file system.

