package org.mosip.nist.nfiq1.mlp;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.mosip.nist.nfiq1.common.IMlp.IAcs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Acs extends Mlp implements IAcs {
	private static final Logger LOGGER = LoggerFactory.getLogger(Acs.class);	
	private static volatile Acs instance;
    public static Acs getInstance() {
        if (instance == null) {
            synchronized (Acs.class) {
                if (instance == null) {
                    instance = new Acs();
                }
            }
        }
        return instance;
    }    
    private Acs()
    {
    	super();
    }
    
	/* Sinusoid activation function and its derivative.  The scaling by .5
	before taking the sine, and the adding of 1 and scaling by .5 after
	taking the sine, cause this function to have the following desirable
	properties: range is [0, 1] (almost same as range of sigmoid, which is
	(0,1)); value at 0 is 1/2 (like sigmoid); and derivative at 0 is 1/4
	(like sigmoid). */
	
    public void acSinusoid(float x, AtomicReference<Float> val, AtomicReference<Float> deriv) {
		double a;

		val.set(0.5f * (1.0f + (float)Math.sin(a = 0.5f * x)));
		deriv.set(0.25f * (float)Math.cos(a));
	}

	/* Sinusoid activation function, value only.
	Input/output arg:
	  p: The address for input of the value, and for output of the
	    result of applying the activation function to this value.
	*/
	public void acVSinusoid(AtomicReferenceArray<Double> p, int index) {
		p.set(index, (double) (0.5d * (1.0d + (double)Math.sin((double)(0.5d * p.get(index))))));
	}

	/* Sigmoid activation function (also called the logistic function) and
	its derivative.  (The idea with SMIN is that it is a large-magnitude
	negative number, such that exp(-SMIN), a large positive number, just
	barely avoids overflow.) */
	public void acSigmoid(double x, AtomicReference<Double> val, AtomicReference<Double> deriv) {
		double v;

		val.set (v = (x >= SMIN ? 1.0d / (1.0d + (double)Math.exp(-x)) : 0.0d));
		deriv.set (v * (1.0d - v));
	}

	/*******************************************************************/
	/* Sigmoid (also called logistic) activation function, value only.
	Input/output arg:
	  p: The address for input of the value, and for output of the
	    result of applying the activation function to this value.
	*/
	public void acVSigmoid(AtomicReferenceArray<Double> p, int index) {
		p.set(index, (p.get(index) >= SMIN ? 1.0d / (1.0d + (double)Math.exp(- p.get(index))) : 0.0d));
	}

	/* A linear activation function and its derivative. */
	public void acLinear(float x, AtomicReference<Double> val, AtomicReference<Double> deriv) {
		val.set (0.25d * (double)x);
		deriv.set (0.25d);
	}

	/* Linear activation function, value only.
	Input/output arg:
	  p: The address for input of the value, and for output of the
	    result of applying the activation function to this value.
	*/
	public void acVLinear(AtomicReferenceArray<Double> p, int index) {
		p.set(index, 0.25f * p.get(index));
	}
}


