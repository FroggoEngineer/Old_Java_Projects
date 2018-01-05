
/** Klassen Complex hanterar komplexa tal */
public class Complex 
{
	private double re, im;
	
	public Complex(double re, double im)
	{
		this.re = re;
		this.im = im;
	}
	
	/** Returnerar den imaginära delen av talet */
	double getIm()
	{
		return im;
	}
	
	/** Returnerar den reella delen av talet */
	double getRe()
	{
		return re;
	}
	
	/** Returnerar absolutbeloppet i kvadrat */
	double getAbs2()
	{
		return (re*re)+(im*im);
	}
	
	/** Adderar det komplexa talet med ett annat */
	void add(Complex c)
	{
		re += c.getRe();
		im +=c.getIm();
	}
	
	/** Multiplicerar det komplexa talet med ett annat */
	void mul(Complex c)
	{
		double tempRe, tempIm;
		tempRe = re*c.getRe() + (im*c.getIm()*-1);
		tempIm = re*c.getIm() + im*c.getRe();
		re = tempRe;
		im = tempIm;
	}
}
