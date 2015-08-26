package hacking;

import java.util.Stack;

/**
 * 
 * @author Ivan Naumovski
 *
 */
public class Person {
    public String name;
    public Stack<Integer> preferred;
    /**
     * The preferences stored according to their ranking.
     * First place is the most preferred match. The value is the id of the person.
     */
    public int[] inversePreferred;

    public Person(String name) {
        this.name = name;
    }
    
    public Person(String name, Integer[] pref) {
    	super();
    	preference(pref);
    }

    public void preference(Integer[] pref) {
        preferred = new Stack<>();
    	for (int i = pref.length-1; i >= 0; i--) {
        	preferred.push(pref[i]);
		}
    	doInversePreference();
    }
    
    private void doInversePreference() {
    	//if we use the transform from GS we can halve the space consumption
    	inversePreferred = new int[2*preferred.size()+1];
    	for (int prefId = 0; prefId < preferred.size(); prefId++) {
            inversePreferred[ preferred.get(prefId) ] = prefId;
        }
    }
    
}
