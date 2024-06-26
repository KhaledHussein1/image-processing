import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.color.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;

public class ImageProcessing extends JFrame implements ActionListener
{
	String title = null;
	Container cp = null;
	float[] marrTemplate = null;
	
	JMenuItem miCopy, miGaussian,miSharpen,miEdgeLog,miEdgeMarr,miRescale,miGrayscale, miCone;
	JMenuItem miOpen,miSave,miExit;
	
	ImagePanel imageSrc = new ImagePanel();
	ImagePanel imageDst = new ImagePanel();;
	JFileChooser fc = new JFileChooser();	
	
	public static void main(String[] args)
	{
		JFrame frame = new ImageProcessing();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);	
	}
	public ImageProcessing()
	{
		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);
		
		JMenu menu = new JMenu("File");
		
		miOpen = new JMenuItem("Open image");
		miOpen.addActionListener(this);
		menu.add(miOpen);
		
		miSave = new JMenuItem("Save image");
		miSave.addActionListener(this);
		miSave.setEnabled(false);
		menu.add(miSave);
		
		menu.addSeparator();
		
		miExit = new JMenuItem("Exit");
		miExit.addActionListener(this);
		menu.add(miExit);
		
		mb.add(menu);
		
		menu = new JMenu("Process");
		
		//Copy Operation
		miCopy = new JMenuItem("Copy");
		miCopy.addActionListener(this);
		miCopy.setEnabled(false);
		menu.add(miCopy);
		
		//Gray Scale Operation
		miGrayscale = new JMenuItem("Gray scale");
		miGrayscale.addActionListener(this);
		miGrayscale.setEnabled(false);
		menu.add(miGrayscale);
		
		//Smooth Operation
		JMenu mSmoothing = new JMenu("Smoothing");
		menu.add(mSmoothing);
		
		miCone = new JMenuItem("Cone");
		miCone.addActionListener(this);
		miCone.setEnabled(false);
		mSmoothing.add(miCone);
		
		miGaussian = new JMenuItem("Gaussian");
		miGaussian.addActionListener(this);
		miGaussian.setEnabled(false);
		mSmoothing.add(miGaussian);
		
		//Sharpen Operation
		miSharpen = new JMenuItem("Sharpen");
		miSharpen.addActionListener(this);
		miSharpen.setEnabled(false);
		menu.add(miSharpen);
		
		
		//Edge Operation
		JMenu edgeMenu = new JMenu("Edge");
		menu.add(edgeMenu);
		
		miEdgeMarr = new JMenuItem("Marr");
        miEdgeMarr.addActionListener(this);
        miEdgeMarr.setEnabled(false);
        edgeMenu.add(miEdgeMarr);
        
        miEdgeLog = new JMenuItem("LoG");
        miEdgeLog.addActionListener(this);
        miEdgeLog.setEnabled(false);
        edgeMenu.add(miEdgeLog);
        
        //Rescale Operation
		miRescale = new JMenuItem("Rescale");
		miRescale.addActionListener(this);
		miRescale.setEnabled(false);
		menu.add(miRescale);
		
		mb.add(menu);
		cp = this.getContentPane();
		
		cp.setLayout(new FlowLayout());
		imageSrc = new ImagePanel();
		imageDst = new ImagePanel();
		
		cp.add(imageSrc);
		cp.add(imageDst);
	}
	
public void actionPerformed(ActionEvent ev)
{
	
	String cmd = ev.getActionCommand();
	
	
	if ("Open image".equals(cmd))
	{
		int retval = fc.showOpenDialog(this);
		if (retval == JFileChooser.APPROVE_OPTION)
		{
			imageSrc.setImage(null);
			imageSrc.paint(cp.getGraphics());
						
			imageDst.setImage(null);
			imageDst.paint(cp.getGraphics());
							
			try
			{
				BufferedImage bi = ImageIO.read(fc.getSelectedFile());
				
				imageSrc.setImage(bi);
				imageSrc.setTopTitle(" Original Image");
				int height = bi.getHeight();
				int width  = bi.getWidth();
				String bt = "Image Dimension = "+
				            Integer.toString(width)+
				            " X "+
				            Integer.toString(height);
				imageSrc.setBottomTitle(bt);
				imageSrc.setPreferredSize(new Dimension(width+100,height+100));	
				
				imageDst.setImage(bi);
				imageDst.setPreferredSize(new Dimension(width+100,height+100));
				imageDst.setImage(null);
				pack();
				cp.repaint(); //Added this to fix image glitch
				
				miCopy.setEnabled(true);				
				miSave.setEnabled(true);
				miGrayscale.setEnabled(false);
				miEdgeLog.setEnabled(false);
				miEdgeMarr.setEnabled(false);
				miGaussian.setEnabled(false);
				miCone.setEnabled(false);
				miSharpen.setEnabled(false);
				miRescale.setEnabled(false);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	else
	if ("Save image".equals(cmd))
	{
		int retval = fc.showSaveDialog(this);
		if (retval == JFileChooser.APPROVE_OPTION)
		{
			if (imageDst == null)
			   imageDst.setImage(imageSrc.getImage());
			try
			{
				ImageIO.write(imageDst.getImage(),"png",fc.getSelectedFile());
			}
			catch (IOException ex)
			{
				ex.printStackTrace();	
			}
			
		}
	}
	else
	if ("Exit".equals(cmd))
	{
		System.exit(0);
	}
	else
	if ("Copy".equals(cmd))
	{
		imageDst.setTopTitle("Copy of Original Image");
		imageSrc.setImage(imageSrc.getImage());
		imageDst.setImage(imageSrc.getImage());
		
		miGrayscale.setEnabled(true);
		miEdgeLog.setEnabled(false);
		miEdgeMarr.setEnabled(false);
		miGaussian.setEnabled(false);
		miCone.setEnabled(false);
		miSharpen.setEnabled(false);
		miRescale.setEnabled(false);
	}
	else
	{
		process(cmd);
	}
	
}

void process (String opName)
{
	BufferedImageOp op = null;
	String t="  ";
	
	if (opName.equals("Cone"))
	{
		t = "Operation - Smoothing - Cone Template";
		float[] data = {0.0f,0.0f,1.0f,0.0f,0.0f,
				        0.0f,2.0f,2.0f,2.0f,0.0f,
				        1.0f,2.0f,5.0f,2.0f,1.0f,
				        0.0f,2.0f,2.0f,2.0f,0.0f,
				        0.0f,0.0f,1.0f,0.0f,0.0f,
				};
		for (int i = 0; i<data.length; i++)
			data[i] = data[i]/25.0f;
		Kernel ker = new Kernel(5,5,data);
		op = new ConvolveOp(ker);	
	}
	else
	if (opName.equals("Gaussian"))
	{
		t = "Operation - Smoothing - Gaussian Template";
		float[] data = {1.0f,4.0f,7.0f,4.0f,1.0f,
				        4.0f,16.0f,26.0f,16.0f,4.0f,
				        7.0f,26.0f,41.0f,26.0f,7.0f,
				        4.0f,16.0f,26.0f,16.0f,4.0f,
				        1.0f,4.0f,7.0f,4.0f,1.0f,
				};
		for (int i = 0; i<data.length; i++)
			data[i] = data[i]/273.0f;
		Kernel ker = new Kernel(5,5,data);
		op = new ConvolveOp(ker);
	}
	else
	if (opName.equals("Sharpen"))
	{
		t = "Operation - Sharpen";
		float[] data = {-1.0f,-1.0f,-1.0f,
				        -1.0f,9.0f,-1.0f,
				        -1.0f,-1.0f,-1.0f,
				};
		
		Kernel ker = new Kernel(3,3,data);
		op = new ConvolveOp(ker);
	}
	else
	if (opName.equals("LoG"))
	{
		t = "Operation - Edge - Laplacian of Gaussian";
		float[] data = {0.0f,-1.0f,0.0f,
				        -1.0f,4.0f,-1.0f,
				        0.0f,-1.0f,0.0f,
				};
		
		Kernel ker = new Kernel(3,3,data);
		op = new ConvolveOp(ker);
	}
	else
	if (opName.equals("Marr"))
	{
		t = "Operation - Edge - Marr";
	    // Prompt the user for window size & sigma
		int size = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter an Odd Positive Integer:","Size of Kernel",JOptionPane.QUESTION_MESSAGE));
	    double sigma = Double.parseDouble(JOptionPane.showInputDialog(null,"Enter a Positive Fractional Number:","Sigma",JOptionPane.QUESTION_MESSAGE));
	    int center = size/2;
	    
		// Create kernel array
		marrTemplate = new float[size*size];

		for (int y = 0; y < size; y++) {
		    for (int x = 0; x < size; x++) {
		        // Compute x and y distances from center
		        int dx = x - center;
		        int dy = y - center;
		        
		        // Compute LoG value
		        double value = ((dx*dx + dy*dy - 2*sigma*sigma) / (sigma*sigma*sigma*sigma)) * Math.exp(-(dx*dx + dy*dy) / 
		        		(2*sigma*sigma)) / (Math.PI * sigma*sigma);
		        
		        // Store LoG value in kernel array
		        int index = y*size + x;
		        marrTemplate[index] = Math.round((float)value*50);
		    }
		}

	    Kernel ker = new Kernel(size, size, marrTemplate);
	    op = new ConvolveOp(ker);  
	}
	else
	if (opName.equals("Rescale"))
	{
		t = "Operation - ReScale";
		op = new RescaleOp(-1.0f,255f,null);	
	}
	else
	if (opName.equals("Gray scale"))
	{
		t = "Operation - Color to B/W";
		op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),null);
		
		miEdgeLog.setEnabled(true);
		miEdgeMarr.setEnabled(true);
		miGaussian.setEnabled(true);
		miCone.setEnabled(true);
		miSharpen.setEnabled(true);
		miRescale.setEnabled(true);
	}
	
	BufferedImage bi = op.filter(imageDst.getImage(),null);
	
	imageDst.setTopTitle(t);
	imageDst.setImage(bi);		
	
	pack();
	cp.repaint(); //Added this to fix copied image glitch
	}

class ImagePanel extends JPanel
{
	BufferedImage image = null;
	String topImageTitle = "    ";
	String bottomImageTitle = "   ";
	
	
	public ImagePanel()
	{
		image = null;
		setPreferredSize(new Dimension(512,512));
			
	}
	public ImagePanel(BufferedImage bi)
	{
		image = bi;
	}
	
	public void setTopTitle(String s)
	{
		topImageTitle = s;
	}
	
	public void setBottomTitle(String s)
	{
		bottomImageTitle = s;
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		if(image != null)
		{
			g2.setColor(Color.blue);
			g2.drawRect(0,0,image.getWidth()+80,image.getHeight()+80);
			g2.drawString(topImageTitle,20,20);
			g2.drawString(bottomImageTitle,20,image.getHeight()+60);
			g2.drawImage(image,40,40,this);	
		}
			
		else 
		{
			g2.drawString(" No Image ",20,20);
			g2.drawRect(0,0,this.getHeight(),this.getWidth());
		}
			
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public void setImage(BufferedImage bi)
	{
		image = bi;
		invalidate();
		repaint();
	}
}
}