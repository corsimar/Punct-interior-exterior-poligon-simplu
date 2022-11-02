import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MyPanel extends JPanel implements MouseListener, ActionListener {
	
	/////////////////////////////// VARIABLES ///////////////////////////////
	int windowW = 1200, windowH = 900, axisCount = 4, axisOffset = 80;
	int strokeSize = 2, circleSize = 4;
	int titleFontSize = 21, textFontSize = 10;
	
	int count = 0, points = 0;
	int pointLimit = 100;
	int[] xPoints = new int[pointLimit], yPoints = new int[pointLimit];
	/////////////////////////////// VARIABLES ///////////////////////////////
	
	public MyPanel() {
		super();
		this.setPreferredSize(new Dimension(windowW, windowH));
		addMouseListener(this);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setPaint(Color.black);
		g2d.setStroke(new BasicStroke(strokeSize));
		
		if(count == 0) {
			g2d.setPaint(Color.white);
			g2d.fillRect(0, 0, windowW, windowH);
			g2d.setPaint(Color.black);
			drawCenteredString(g2d, "Creati puncte apasand pe ecran.", new Rectangle(0, 0, windowW, axisOffset), new Font("Sans Serif", Font.BOLD, titleFontSize));
		}
		if(count > 0) {
			g2d.fillOval(xPoints[count - 1] - circleSize / 2, yPoints[count - 1] - circleSize / 2, circleSize, circleSize);
			g2d.setFont(new Font("Sans Serif", Font.BOLD, textFontSize));
			g2d.drawString("(" + xPoints[count - 1] + ", " + yPoints[count - 1] + ")", xPoints[count - 1], yPoints[count - 1] - circleSize / 2);
		}
		if(count > 1) {
			g2d.setPaint(Color.black);
			g2d.drawLine(xPoints[count - 2], yPoints[count - 2], xPoints[count - 1], yPoints[count - 1]);
		}
		if(count > 2) {
			g2d.setPaint(Color.white);
			g2d.fillRect(0, 0, windowW, axisOffset);
			g2d.setPaint(Color.black);
			drawCenteredString(g2d, "Apasati tasta ENTER pentru a finaliza poligonul simplu.", new Rectangle(0, 0, windowW, axisOffset), new Font("Sans Serif", Font.BOLD, titleFontSize));
		}
		if(count == -pointLimit) {
			g2d.fillOval(xPoints[pointLimit - 1] - circleSize / 2, yPoints[pointLimit - 1] - circleSize / 2, circleSize, circleSize);
			g2d.setFont(new Font("Sans Serif", Font.BOLD, textFontSize));
			g2d.drawString("(" + xPoints[pointLimit - 1] + ", " + yPoints[pointLimit - 1] + ")", xPoints[pointLimit - 1], yPoints[pointLimit - 1] - circleSize);
			g2d.drawString("M", xPoints[pointLimit - 1], yPoints[pointLimit - 1] + circleSize + g2d.getFontMetrics().getHeight() / 2);
			g2d.setPaint(Color.white);
			g2d.fillRect(0, 0, windowW, axisOffset);
			g2d.setPaint(Color.black);
			solveProblem(g2d);
		}
		if(count < 0 && count != -pointLimit) {
			g2d.drawLine(xPoints[0], yPoints[0], xPoints[-count - 1], yPoints[-count - 1]);
			g2d.setPaint(Color.white);
			g2d.fillRect(0, 0, windowW, axisOffset);
			g2d.setPaint(Color.black);
			drawCenteredString(g2d, "Apasati pe ecran pentru a crea punctul M.", new Rectangle(0, 0, windowW, axisOffset), new Font("Sans Serif", Font.BOLD, titleFontSize));
			count = -pointLimit;
		}
		
		g2d.setPaint(new Color(207, 207, 207));
		g2d.setStroke(new BasicStroke(strokeSize / 4));
		for(int i = 0; i < axisCount; i++) {
			g2d.drawLine(0, axisOffset + windowH / axisCount * (i + 1), windowW, axisOffset + windowH / axisCount * (i + 1));
			g2d.drawLine(windowW / axisCount * (i + 1), axisOffset, windowW / axisCount * (i + 1), windowH);
		}
	}
	
	public void endPolygon() {
		if(count > 2) {
			points = count;
			count = -count;
			repaint();
		}
	}
	
	public void drawCenteredString(Graphics2D g2d, String text, Rectangle rect, Font font) {
		FontMetrics metrics = g2d.getFontMetrics(font);
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		g2d.setFont(font);
		g2d.drawString(text, x, y);
	}
	
	int determinant(int x1, int y1, int x2, int y2, int x3, int y3) {
		return x1 * y2 + x2 * y3 + x3 * y1 - y2 * x3 - y3 * x1 - y1 * x2;
	}
	
	void solveProblem(Graphics2D g2d) {
		int c = 0, i;
		Point A = new Point(0, 0);
		Point B = new Point(0, 0);
		xPoints[points + 1] = xPoints[0]; yPoints[points + 1] = yPoints[0];
		for(i = 0; i < points; i++) {
			if((yPoints[i] == yPoints[i + 1] && yPoints[i + 1] == yPoints[pointLimit - 1]) && ((xPoints[pointLimit - 1] - xPoints[i]) * (xPoints[pointLimit - 1] - xPoints[i + 1])) <= 0) {
				c = -1;
				break;
			}
			if(yPoints[i] > yPoints[i + 1]) {
				A.setPointPos(xPoints[i], yPoints[i]);
				B.setPointPos(xPoints[i + 1], yPoints[i + 1]);
			}
			else if(yPoints[i] < yPoints[i + 1]) {
				A.setPointPos(xPoints[i + 1], yPoints[i + 1]);
				B.setPointPos(xPoints[i], yPoints[i]);
			}
			if(yPoints[i] != yPoints[i + 1] && yPoints[pointLimit - 1] > B.getPointPos()[1] && yPoints[pointLimit - 1] < A.getPointPos()[1]) {
				if(determinant(A.getPointPos()[0], A.getPointPos()[1], xPoints[pointLimit -1], yPoints[pointLimit - 1], B.getPointPos()[0], B.getPointPos()[1]) > 0)
					c++;
				else if(determinant(A.getPointPos()[0], A.getPointPos()[1], xPoints[pointLimit -1], yPoints[pointLimit - 1], B.getPointPos()[0], B.getPointPos()[1]) > 0) {
					c = -1;
					break;
				}
			}
			else if(yPoints[i] != yPoints[i + 1] && yPoints[pointLimit - 1] == A.getPointPos()[1]) {
				if(xPoints[pointLimit - 1] < A.getPointPos()[1])
					c++;
				if(xPoints[pointLimit - 1] == A.getPointPos()[1]) {
					c = -1;
					break;
				}
			}
			else if(yPoints[i] != yPoints[i + 1] && yPoints[pointLimit - 1] == B.getPointPos()[1] && xPoints[pointLimit - 1] == B.getPointPos()[0]) {
				c = -1;
				break;
			}
		}
		if(c < 0)
			drawCenteredString(g2d, "Punctul M este pe frontiera.", new Rectangle(0, 0, windowW, axisOffset), new Font("Sans Serif", Font.BOLD, titleFontSize));
		else
			if(c % 2 == 0)
				drawCenteredString(g2d, "Punctul M se afla in exterior.", new Rectangle(0, 0, windowW, axisOffset), new Font("Sans Serif", Font.BOLD, titleFontSize));
			else 
				drawCenteredString(g2d, "Punctul M se afla in interior.", new Rectangle(0, 0, windowW, axisOffset), new Font("Sans Serif", Font.BOLD, titleFontSize));
	}
	
	void print(int message) {
		System.out.println(message);
	}
	
	void print(String message) {
		System.out.println(message);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(count >= 0) {
			xPoints[count] = e.getX();
			yPoints[count] = e.getY();
			count++;
			repaint();
		}
		else if(count == -pointLimit) {
			xPoints[pointLimit - 1] = e.getX();
			yPoints[pointLimit - 1] = e.getY();
			repaint();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
