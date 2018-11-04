package com.bugalu.nlp.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PredefinedCategories {
	
//	public static final Logger log = Logger.getLogger(PredefinedCategories.class);

	public static final Map<String, Set<String>> categories;
	static {
		Map<String, Set<String>> placeType = new HashMap<>();

		Map<String, Set<String>> cocktail = new HashMap<>();
		Map<String, Set<String>> brunch = new HashMap<>();
		Map<String, Set<String>> mexican = new HashMap<>();

		cocktail.put("cocktails", new HashSet<>(Arrays.asList("Cocktails,booze,drinks".split(","))));
		cocktail.put("food", new HashSet<>(Arrays.asList("food,meal,dish,menu,portion".split(","))));
		cocktail.put("atmosphere", new HashSet<>(Arrays.asList("vibe,vibes,atmosphere,environment,ambience".split(","))));
		cocktail.put("crowdness", new HashSet<>(Arrays.asList("overcrowded,packed,crowded,busy".split(","))));
		cocktail.put("wait time", new HashSet<>(Arrays.asList("wait,line,waitlist,min,minutes".split(","))));
		cocktail.put("expensive", new HashSet<>(Arrays.asList("pricey,expensive,overpriced".split(","))));
		cocktail.put("Loudness", new HashSet<>(Arrays.asList("loud,quite".split(","))));
		cocktail.put("service", new HashSet<>(Arrays.asList("service,staff,bartender,bartenders".split(","))));
		cocktail.put("live music", new HashSet<>());
		cocktail.put("exterior", new HashSet<>());

		brunch.put("french toast", new HashSet<>());
		brunch.put("big portion", new HashSet<>());
		brunch.put("food", new HashSet<>(Arrays.asList("food,meal,dish,menu,portion".split(","))));
		brunch.put("atmosphere", new HashSet<>(Arrays.asList("vibe,vibes,atmosphere,environment".split(","))));
		brunch.put("crowdness", new HashSet<>(Arrays.asList("overcrowded,packed,crowded,busy".split(","))));
		brunch.put("wait time", new HashSet<>(Arrays.asList("wait,line,waitlist,min,minutes".split(","))));
		brunch.put("expensive", new HashSet<>(Arrays.asList("pricey,expensive,overpriced".split(","))));
		brunch.put("loudness", new HashSet<>(Arrays.asList("loud,quite".split(","))));
		brunch.put("service", new HashSet<>(Arrays.asList("service,staff,bartender,bartenders".split(","))));
		brunch.put("live music", new HashSet<>());
		brunch.put("exterior", new HashSet<>(Arrays.asList("cute place".split(","))));

		mexican.put("cocktails", new HashSet<>(Arrays.asList("Cocktails,booze,drinks".split(","))));
		mexican.put("food", new HashSet<>(Arrays.asList("food,meal,dish,menu,portion".split(","))));
		mexican.put("atmosphere", new HashSet<>(Arrays.asList("vibe,vibes,atmosphere,environment".split(","))));
		mexican.put("crowdness", new HashSet<>(Arrays.asList("overcrowded,packed,crowded,busy".split(","))));
		mexican.put("wait time",new HashSet<>( Arrays.asList("wait,line,waitlist,min,minutes".split(","))));
		mexican.put("expensive",new HashSet<>( Arrays.asList("pricey,expensive,overpriced".split(","))));
		mexican.put("loudness", new HashSet<>(Arrays.asList("loud,quite".split(","))));
		mexican.put("service", new HashSet<>(Arrays.asList("service,staff,bartender,bartenders".split(","))));
		mexican.put("live music", new HashSet<>());
		mexican.put("exterior", new HashSet<>(Arrays.asList("cute place".split(","))));

		placeType.putAll( cocktail);
		placeType.putAll(brunch);
		placeType.putAll( mexican);
		categories = Collections.unmodifiableMap(placeType);
	}

}
