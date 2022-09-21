package esoteric.jsfuck;

import java.util.Arrays;
import java.util.List;

import esoteric.jsfuck.optimiser.JSNodeConverter;
import esoteric.jsfuck.optimiser.JSPrimitivesMerger;
import model.Optimiser;
import model.OptimiserImpl;

public class JSFuckOptimiser extends OptimiserImpl {
	public static final int 
		PM = 1 << 0,	// primitives merger
		NC = 1 << 1;	// nodes converter
	public static final List<Class<? extends Optimiser>> OPTIMISERS = Arrays.asList(
			JSPrimitivesMerger.class,
			JSNodeConverter.class
	);
	
	public JSFuckOptimiser() {
		this(PM | NC);
	}
	
	public JSFuckOptimiser(int optimisations) {
		super(true, optimisations);
	}

	@Override
	public List<Class<? extends Optimiser>> getOptimisers() {
		return OPTIMISERS;
	}
}