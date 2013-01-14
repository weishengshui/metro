package com.chinarewards.metro.core.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageServlet extends HttpServlet{

	private static final long serialVersionUID = -2302729181481460112L;
	private Font font = new Font("Times New Roman",Font.PLAIN,17);
	Color getRandColor(int fc,int bc){
        Random random = new Random();
        if(fc>255) fc=255;
        if(bc>255) bc=255;
        int r=fc+random.nextInt(bc-fc);
        int g=fc+random.nextInt(bc-fc);
        int b=fc+random.nextInt(bc-fc);
        return new Color(r,g,b);
        }
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Pragma","No-cache");
		response.setHeader("Cache-Control","no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		int width=60, height=18;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200,250));
		g.fillRect(1, 1, width-1, height-1);
		g.setColor(new Color(102,102,102));
		g.drawRect(0, 0, width-1, height-1);
		g.setFont(font);
		g.setColor(getRandColor(160, 200));
		for (int i=0;i<155;i++)
		{
		 int x = random.nextInt(width-1);
		 int y = random.nextInt(height-1);
		        int xl = random.nextInt(6)+1;
		        int yl = random.nextInt(12)+1;
		 g.drawLine(x,y,x+xl,y+yl);
		}
		String sRand="";
		for (int i=0;i<4;i++){
		    String rand=String.valueOf(random.nextInt(10));
		    sRand+=rand;
		    g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
		    g.drawString(rand,13*i+6,16);
		}

		request.getSession().setAttribute("rand",sRand);

		g.dispose();

		ImageIO.write(image, "JPEG", response.getOutputStream());
	}
}
