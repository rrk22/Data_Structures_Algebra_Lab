/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 23 Oct 08; 27 Mar 09; 06 Aug 10
 *          30 Dec 13
 */

public class Cons
{
    // instance variables
    private Object car;
    private Cons cdr;
    private Cons(Object first, Cons rest)
       { car = first;
         cdr = rest; }
    public static Cons cons(Object first, Cons rest)
      { return new Cons(first, rest); }
    public static boolean consp (Object x)
       { return ( (x != null) && (x instanceof Cons) ); }
// safe car, returns null if lst is null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }
// safe cdr, returns null if lst is null
    public static Cons rest(Cons lst) {
      return ( (lst == null) ? null : lst.cdr  ); }
    public static Object second (Cons x) { return first(rest(x)); }
    public static Object third (Cons x) { return first(rest(rest(x))); }
    public static void setfirst (Cons x, Object i) { x.car = i; }
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }
   public static Cons list(Object ... elements) {
       Cons list = null;
       for (int i = elements.length-1; i >= 0; i--) {
           list = cons(elements[i], list);
       }
       return list;
   }
    // access functions for expression representation
    public static Object op  (Cons x) { return first(x); }
    public static Object lhs (Cons x) { return first(rest(x)); }
    public static Object rhs (Cons x) { return first(rest(rest(x))); }
    public static boolean numberp (Object x)
       { return ( (x != null) &&
                  (x instanceof Integer || x instanceof Double) ); }
    public static boolean integerp (Object x)
       { return ( (x != null) && (x instanceof Integer ) ); }
    public static boolean floatp (Object x)
       { return ( (x != null) && (x instanceof Double ) ); }
    public static boolean stringp (Object x)
       { return ( (x != null) && (x instanceof String ) ); }

    // convert a list to a string for printing
    public String toString() {
       return ( "(" + toStringb(this) ); }
    public static String toString(Cons lst) {
       return ( "(" + toStringb(lst) ); }
    private static String toStringb(Cons lst) {
       return ( (lst == null) ?  ")"
                : ( first(lst) == null ? "()" : first(lst).toString() )
                  + ((rest(lst) == null) ? ")" 
                     : " " + toStringb(rest(lst)) ) ); }

    public boolean equals(Object other) { return equal(this,other); }

    // tree equality
public static boolean equal(Object tree, Object other) {
    if ( tree == other ) return true;
    if ( consp(tree) )
        return ( consp(other) &&
                 equal(first((Cons) tree), first((Cons) other)) &&
                 equal(rest((Cons) tree), rest((Cons) other)) );
    return eql(tree, other); }

    // simple equality test
public static boolean eql(Object tree, Object other) {
    return ( (tree == other) ||
             ( (tree != null) && (other != null) &&
               tree.equals(other) ) ); }

// member returns null if requested item not found
public static Cons member (Object item, Cons lst) {
  if ( lst == null )
     return null;
   else if ( item.equals(first(lst)) )
           return lst;
         else return member(item, rest(lst)); }

public static Cons union (Cons x, Cons y) {
  if ( x == null ) return y;
  if ( member(first(x), y) != null )
       return union(rest(x), y);
  else return cons(first(x), union(rest(x), y)); }

public static boolean subsetp (Cons x, Cons y) {
    return ( (x == null) ? true
             : ( ( member(first(x), y) != null ) &&
                 subsetp(rest(x), y) ) ); }

public static boolean setEqual (Cons x, Cons y) {
    return ( subsetp(x, y) && subsetp(y, x) ); }

    // combine two lists: (append '(a b) '(c d e))  =  (a b c d e)
public static Cons append (Cons x, Cons y) {
  if (x == null)
     return y;
   else return cons(first(x),
                    append(rest(x), y)); }

    // look up key in an association list
    // (assoc 'two '((one 1) (two 2) (three 3)))  =  (two 2)
public static Cons assoc(Object key, Cons lst) {
  if ( lst == null )
     return null;
  else if ( key.equals(first((Cons) first(lst))) )
      return ((Cons) first(lst));
          else return assoc(key, rest(lst)); }

    public static int square(int x) { return x*x; }
    public static int pow (int x, int n) {
        if ( n <= 0 ) return 1;
        if ( (n & 1) == 0 )
            return square( pow(x, n / 2) );
        else return x * pow(x, n - 1); }

public static Cons formulas = 
    list( list( "=", "s", list("*", new Double(0.5),
                               list("*", "a",
                                list("expt", "t", new Integer(2))))),
          list( "=", "s", list("+", "s0", list( "*", "v", "t"))),
          list( "=", "a", list("/", "f", "m")),
          list( "=", "v", list("*", "a", "t")),
          list( "=", "f", list("/", list("*", "m", "v"), "t")),
          list( "=", "f", list("/", list("*", "m",
                                         list("expt", "v", new Integer(2))),
                               "r")),
          list( "=", "h", list("-", "h0", list("*", new Double(4.94),
                                               list("expt", "t",
                                                    new Integer(2))))),
          list( "=", "c", list("sqrt", list("+",
                                            list("expt", "a",
                                                 new Integer(2)),
                                            list("expt", "b",
                                                 new Integer(2))))),
          list( "=", "v", list("*", "v0",
                               list("-", new Double(1.0),
                                    list("exp", list("/", list("-", "t"),
                                                     list("*", "r", "c"))))))
          );

    // Note: this list will handle most, but not all, cases.
    // The binary operators - and / have special cases.
public static Cons opposites = 
    list( list( "+", "-"), list( "-", "+"), list( "*", "/"),
          list( "/", "*"), list( "sqrt", "expt"), list( "expt", "sqrt"),
          list( "log", "exp"), list( "exp", "log") );

public static void printanswer(String str, Object answer) {
    System.out.println(str +
                       ((answer == null) ? "null" : answer.toString())); }

    // ****** your code starts here ******


public static Cons findpath(Object item, Object cave) {
	
	//System.out.println("   " + cave);
	//cons
	if (consp(cave))
	{
		Cons cons_cave = (Cons)cave;
		
		if ((findpath(item, first(cons_cave)))!=null)
		{
			return cons("first",(findpath(item, first(cons_cave))));
		}
		
		else if ((findpath(item, rest(cons_cave)))!=null)
		{
			return cons("rest",(findpath(item, rest(cons_cave))));
		}
		
		else
		{
			return null;
		}
		
	}
	
	//not cons
	else
	{
		if (item.equals(cave))
			return list("done");
		
		return null;
		
	}
 }

public static Object follow(Cons path, Object cave) {
	
	if (consp(cave))
	{
		if (first(path).equals("first"))
		{
			return follow(rest(path), first((Cons)cave));
		}
		else
		{
			return follow(rest(path), rest((Cons)cave));
		}
	}
	else
	{
		return cave;
	}	
 }

public static Object corresp(Object item, Object tree1, Object tree2) {
	Object path = findpath(item, tree1);
	Object return_item = follow((Cons)path, tree2);
	return return_item;
}

public static Cons solve(Cons e, String v) {
	
	
	if (lhs(e).equals(v))
	{
		return e;		
	}
		
	else if	(rhs(e).equals(v))
	{
		return solve(list(op(e),rhs(e),lhs(e)), v);
	}
	
	else if (!rhs(e).equals(v) && !consp(rhs(e)))
	{
		return null;
	}
	
	else if (consp(rhs(e)))
	{
		
		
		Cons solvereturn = null;
		
		Object left = lhs(e);
		Cons right = (Cons)rhs(e);
		Object operator = op(right);
		Object NewOperator = second(assoc(operator, opposites));
		
		//helper objects
		Object needed = ContainingVariable(v, left, right);
		Object not_needed = NonContainingVariable(v, left, right);
		
		Object new_LHS = null;
		Object new_RHS = null;
		
		
		//  (-t)
		

		
		if (size(right)==3) //binary
		{
			if (operator.equals("+"))
			{
			//	new_LHS = list(NewOperator, left, not_needed);
			//	new_RHS = rhs(right);
				Object innerLHS = lhs(right);
				Object innerRHS = rhs(right);
				
				Object inner_needed = null;
				Object inner_not_needed = null;
				//
				if (consp(innerLHS))
				{
					if (ConsContains(v, (Cons)innerLHS))
					{
						inner_needed = innerLHS;
						inner_not_needed = innerRHS;
					}
					else
					{
						inner_needed = innerRHS;
						inner_not_needed = innerLHS;						
					}
				}
				
				else
				{
					if (innerLHS.equals(v))
					{
						inner_needed = innerLHS;
						inner_not_needed = innerRHS;						
					}
					else
					{
						inner_needed = innerRHS;
						inner_not_needed = innerLHS;						
					}
				}
				//			
				
				new_LHS = list("-", left, inner_not_needed);
				new_RHS = inner_needed;					
								
		//		System.out.println("new LHS " + new_LHS + " new RHS " + new_RHS);
			}
			else if (operator.equals("*"))
			{
				Object innerLHS = lhs(right);
				Object innerRHS = rhs(right);

			//	Object inner_needed = ContainingVariable(v, innerLHS, innerRHS);
			//	Object inner_not_needed = NonContainingVariable(v, innerLHS, innerRHS);
				
				Object inner_needed = null;
				Object inner_not_needed = null;
				
				//checking side contains the variable and which side doesn't
				
				//
				if (consp(innerLHS))
				{
					if (ConsContains(v, (Cons)innerLHS))
					{
						inner_needed = innerLHS;
						inner_not_needed = innerRHS;
					}
					else
					{
						inner_needed = innerRHS;
						inner_not_needed = innerLHS;						
					}
				}
				
				else
				{
					if (innerLHS.equals(v))
					{
						inner_needed = innerLHS;
						inner_not_needed = innerRHS;						
					}
					else
					{
						inner_needed = innerRHS;
						inner_not_needed = innerLHS;						
					}
				}
				//
				
				
	//			System.out.println("needed: " + inner_needed);
	//			System.out.println("not needed: " + inner_not_needed);
			//	System.out.println("innerRHS: " + innerRHS);
			//	System.out.println("innerLHS: " + innerLHS);
			
				new_LHS = list("/", left, inner_not_needed);
				new_RHS = inner_needed;
				
	//			System.out.println("new LHS " + new_LHS + " new RHS " + new_RHS);
								
			}
			else if (operator.equals("-"))
			{
				Object innerLHS = lhs(right);
				Object innerRHS = rhs(right);

				Object inner_needed = null;
				Object inner_not_needed = null;
				//
				if (consp(innerLHS))
				{
					if (ConsContains(v, (Cons)innerLHS))
					{
						inner_needed = innerLHS;
						inner_not_needed = innerRHS;
					}
					else
					{
						inner_needed = innerRHS;
						inner_not_needed = innerLHS;						
					}
				}
				
				else
				{
					if (innerLHS.equals(v))
					{
						inner_needed = innerLHS;
						inner_not_needed = innerRHS;						
					}
					else
					{
						inner_needed = innerRHS;
						inner_not_needed = innerLHS;						
					}
				}
				//			
				if (inner_needed.equals(innerLHS))
				{
					new_LHS = list("+",left, innerRHS);
					new_RHS = list(innerLHS);
				}
				
				else if (inner_not_needed.equals(innerLHS))
				{
					new_LHS = list("-", innerLHS, left);
					new_RHS = innerRHS;
				}
			}
			
			
			else if (operator.equals("/"))
			{
				Object innerLHS = lhs(right);
				Object innerRHS = rhs(right);
				
				Object inner_needed = null;
				Object inner_not_needed = null;
				//
				if (consp(innerLHS))
				{
					if (ConsContains(v, (Cons)innerLHS))
					{
						inner_needed = innerLHS;
						inner_not_needed = innerRHS;
					}
					else
					{
						inner_needed = innerRHS;
						inner_not_needed = innerLHS;						
					}
				}
				
				else
				{
					if (innerLHS.equals(v))
					{
						inner_needed = innerLHS;
						inner_not_needed = innerRHS;						
					}
					else
					{
						inner_needed = innerRHS;
						inner_not_needed = innerLHS;						
					}
				}
				//	
				
				if (inner_needed.equals(innerLHS))
				{

					new_LHS = list("*", left, innerRHS);
					new_RHS = innerLHS;
					
				}
				
				else if (inner_not_needed.equals(innerLHS))
				{

					new_LHS = innerRHS;
					new_RHS = list("/", innerLHS, left);
					
					
					//switching test
					new_RHS = innerRHS;
					new_LHS = list("/", innerLHS, left);
				}							
			}
			
			else if (operator.equals("expt"))
			{
	
				new_LHS = list("sqrt", left);
				new_RHS = lhs(right);
			}
			
			else if (operator.equals("sqrt"))  //check
			{
				new_LHS = list("expt", left, 2);
				new_RHS = lhs(right);
			}
			else if (operator.equals("exp"))  //check
			{
				new_LHS = list("log", left);
				new_RHS = lhs(right);			
			}
			else if (operator.equals("log"))  //check
			{
				new_LHS = list("exp", left);
				new_RHS = lhs(right);				
			}
			
			//final return of changed equation
	//		System.out.println("step: " + list("=", new_LHS, new_RHS) + " solve for: " + v);
			
			if (consp(new_RHS))
			{
	//			System.out.println(" new rhs size: " + size((Cons)new_RHS));
			}
			return solve(list("=", new_LHS, new_RHS), v);
		}
		
		else if ( (size(right)==2))
		{
	//		System.out.println("2222222222222222222 "  + right);
			//exp
			if (operator.equals("-"))
			{
				Object New_LHS = list("-", left);
				Object New_RHS = second(right);
				return solve(list("=", New_LHS, New_RHS), v);			
			}
			
			else if (operator.equals("exp"))
			{
				Object New_LHS = list("log", left);
				Object New_RHS = second(right);
	//			System.out.println("new left side: " + New_LHS);
	//			System.out.println("new right side: " + New_RHS);
	//			System.out.println("new right side size : " + size((Cons)New_RHS));
				return solve(list("=", New_LHS, New_RHS), v);
			}
			
			else if (operator.equals("sqrt"))
			{
				Object New_LHS = list("expt", left, "2");
				Object New_RHS = second(right);
				return solve(list("=", New_LHS, New_RHS), v);		
			}

		}
		else if (size (right)==1)
		{
	//		System.out.println("11111111111111 " + right);
			
			return solve(list("=", left, first(right)), v);
		}

		

	
	}
	



	return null;
	
}

//solve helper functions

public static boolean ConsContains(Object o, Cons c)
{
	boolean b = false;
	
	while (first(c)!=null)
	{
		boolean check = false;
		
		if (consp(first(c)))
		{
			check = ConsContains(o, (Cons)first(c));
		}
		else
		{
			if (first(c).equals(o))
			{
				check = true;
			}
		}
		
		if (check)
		{
			b = true;
		}
		
		c = rest(c);
	}
	
	return b;
}

public static Object ContainingVariable(Object variable, Object a, Cons b)
{
	Object returnobject = null;
	
	if (a.equals(variable))
	{
		returnobject = a;
	}
	else
	{
		returnobject = b;
	}
	return returnobject;
}

public static Object ContainingVariable(Object variable, Object a, Object b)
{
	Object returnobject = null;
	
	if (b.equals(variable))
	{
		returnobject = b;
	}
	else
	{
		returnobject = a;
	}
	return returnobject;
}


public static Object NonContainingVariable(Object variable, Object a, Cons b)
{
	if (ContainingVariable(variable,a,b).equals(a))
	return b;
	else
	return a;
}

public static Object NonContainingVariable(Object variable, Object a, Object b)
{
	if (a.equals(variable))
	return b;
	else
	return a;
}

public static Double solveit (Cons equations, String var, Cons values) {
	Cons formula = null;
		
		//returns correct equation based on question given
		Cons proper_equation = solveitformula(equations, var, values);
	
	
		proper_equation = solve(proper_equation, var);
		
		Cons evaluating_equation = (Cons)rhs(proper_equation);
//		System.out.println("evaluating_equation: " + evaluating_equation);
//		System.out.println("values " + values);
	//	System.out.println("equation used: " + evaluating_equation);
		String s = "" + eval(evaluating_equation, values);

	return eval(evaluating_equation, values);

}

public static Cons solveitformula (Cons equations, String var, Cons values)
{
	Cons return_eq = null;
	
	Cons valuelist = null;
	
	while(first(values)!= null)
	{
		valuelist = cons((first((Cons)first(values))), valuelist);
		values = rest(values);
	}
	
	valuelist = cons(var, valuelist);
	
	
	//ConsContains(Object o, Cons c)
	// for each equation in equations, if the equation contains all values in valuelist, that equation equals return_eq
	
	while (first(equations)!=null) //while you can still check equations, or the return equation has been found
	{
		Cons current_equation = (Cons)first(equations);
	//	System.out.println("current equation: " + current_equation);

		boolean equation_has_all_values = true;
		
		Cons checkvaluelist = valuelist;
		
		while (first(checkvaluelist)!= null)
		{
			Object current_value = first(checkvaluelist);
	//		System.out.println("      current valuelist: " + current_value);
			
			
			if (!ConsContains(current_value, current_equation))
			{
				equation_has_all_values = false;
			}
			
			
			
			
			
			checkvaluelist = rest(checkvaluelist);
		}	
		
		if (equation_has_all_values)
		{
			return_eq = current_equation;
		}
		
		equations  = rest(equations);
		
	}
	
	
	
	return return_eq;
}

/*
 *public static Cons formulas = 
    list( list( "=", "s", list("*", new Double(0.5),
                               list("*", "a",
                                list("expt", "t", new Integer(2))))),
          list( "=", "s", list("+", "s0", list( "*", "v", "t"))),
          list( "=", "a", list("/", "f", "m")),
          list( "=", "v", list("*", "a", "t")),
          list( "=", "f", list("/", list("*", "m", "v"), "t")),
          list( "=", "f", list("/", list("*", "m",
                                         list("expt", "v", new Integer(2))),
                               "r")),
          list( "=", "h", list("-", "h0", list("*", new Double(4.94),
                                               list("expt", "t",
                                                    new Integer(2))))),
          list( "=", "c", list("sqrt", list("+",
                                            list("expt", "a",
                                                 new Integer(2)),
                                            list("expt", "b",
                                                 new Integer(2))))),
          list( "=", "v", list("*", "v0",
                               list("-", new Double(1.0),
                                    list("exp", list("/", list("-", "t"),
                                                     list("*", "r", "c"))))))
          );
 *
 */





/*
 *   printanswer("Tower: " , solveit(formulas, "h0", list(list("h", new Double(0.0)), list("t", new Double(4.0)))));
 */

 
    // Include your functions vars and eval from the previous assignment.
    // Modify eval as described in the assignment.
    
    
public static Cons vars (Object expr) {
	

	if (consp(expr))
	{
		return union(vars(first((Cons)expr)), vars(rest((Cons)expr)));
	}
	
	else if (stringp (expr) && !containsOperation((String)expr))
	{
		String s = (String)expr;
		return cons(expr,null);
	}
	else
	{
		return null;
	}
	

}

//vars helper function
public static boolean containsOperation(String s)
{
	boolean isInside = false;
	Cons check = list("+", "sum", "-", "difference", "*", "product", "/", "quotient", "expt", "power", "=", "exp", "sqrt", "log");   //add operations to avoid
	
	while (first(check)!= null)
	{
		if (((String)first(check)).equals(s))
		{
			isInside = true;
		}
		check = rest(check);
	}
	
	return isInside;
}
    //extra size method
public static int size (Cons lst)
{
	int size = 0;
	
	while (first(lst)!= null)
	{
		size++;
		lst = rest(lst);
	}
		
	return size;
}
//helper methods for eval
                
                    
public static Cons reverseCons (Cons c)
{
	Cons returnCons = null;
	
	while (first(c) != null)
	{
		returnCons = cons (first(c), returnCons);
		c = rest(c);	
	}
		
	return returnCons;

}


public static Cons singleConsReplace(Cons formula, Cons bindings)
{
	Cons returnCons = null;
	
	while (first(formula)!= null)
	{
		Object o = first(formula);
		if (assoc(o, bindings)!=null)
		{
			o = second(assoc(o, bindings));
		}
		
		returnCons = cons(o, returnCons);
		formula = rest(formula);
	}
	
	returnCons = reverseCons(returnCons);
	return returnCons;
}


public static Double eval (Object tree, Cons bindings) {
	
	if(consp(tree))
	{
			
		if (size((Cons)tree)==2)
		{
			Object operator = op((Cons)tree);
			Object o = second((Cons)tree);
			
			Double doubleO = eval(o, bindings);
		//	System.out.println("doubleo: " + doubleO);
			
			Double inner = eval(lhs((Cons) tree), bindings);
	//		System.out.println("inner: " + inner);
			
			
			if (operator.equals("-"))
			{
			/*	if (integerp(o))
				{	
					return new Double(-1*(Integer)o);
				}
				else if (floatp(o))
				{
					return -1*(Double)o;
				}
				else
				{
					Cons association = assoc(o, bindings);
					return -1*(Double)second(association);
				} */
				
				
				return -1*inner;
								
			}
			else if (operator.equals("sqrt"))
			{
				return Math.sqrt(inner);			
			}
			else if (operator.equals("log"))
			{	
				return Math.log(inner);
			}
			else if (operator.equals("exp"))
			{
				return Math.exp(inner);
			}

		}
		else
		{
			Object operator = op((Cons)tree);
			Object operand1 = lhs((Cons)tree);
			Object operand2 = rhs((Cons)tree);
			
			Double op1 = 0.0;
			Double op2 = 0.0;
		//	Double rhs = eval(rhs((Cons) tree), bindings);
			if (consp(operand1))
			{
				op1 = eval(operand1, bindings);
			}
			else if (integerp(operand1))
			{
				op1 = new Double((Integer)operand1);
			}
			else if (floatp(operand1))
			{
				op1 = (Double)operand1;
			}
			else if (stringp(operand1))
			{
				Cons association = assoc(operand1, bindings);
				op1 = (Double)second(association);
				
				if (op1==null)
				{
					op1 = Double.parseDouble(operand1 + "");
				}
			}
			
			//check 2nd operand
			if (consp(operand2))
			{
				op2=eval(operand2, bindings);
			}
			
			else if (integerp(operand2))
			{
				op2 = new Double((Integer)operand2);
			}
			else if (floatp(operand2))
			{
				op2 = (Double)operand2;
			}
			else if (stringp(operand2))
			{
			//	System.out.println("yes!");
				Cons association = assoc(operand2, bindings);
				op2 = (Double)second(association);
				
				if (op2==null)  // if association is null, simply parse operand to Double
				{
					op2 = Double.parseDouble(operand2 + "");
				}				
			}
			
			//final operations
			if (operator.equals("+"))
			{	
				return op1+op2;
			}
			else if (operator.equals("-"))
			{	
				return op1-op2;
			}
			else if (operator.equals("*"))
			{	
				return op1*op2;
			}
			else if (operator.equals("/"))
			{
				return op1/op2;
			}
			else if (operator.equals("expt"))
			{
			//	System.out.println("operator: " + operator + " op1: " + op1 + " op2: " + op2);
				return Math.pow(op1,op2);
				
			}
			else
			{	
				return 0.0;
			}			
			
		}
	}
	
	
	else if (tree != null)
	{
	//	System.out.println("lasttttttttttttt");

		if (integerp(tree))
			return new Double((Integer)tree);
		else if (integerp(tree))
		{
			return (Double)tree;
		}
		else
		{
			Cons association = assoc(tree, bindings);
			return (Double)second(association);
		}
	}
	
//	System.out.println("try again");
	return 0.0;

}





    // ****** your code ends here ******

    public static void main( String[] args ) {

        Cons cave = list("rocks", "gold", list("monster"));
        Cons path = findpath("gold", cave);
        printanswer("cave = " , cave);
        printanswer("path = " , path);
        printanswer("follow = " , follow(path, cave));

        Cons caveb = list(list(list("green", "eggs", "and"),
                               list(list("ham"))),
                          "rocks",
                          list("monster",
                               list(list(list("gold", list("monster"))))));
        Cons pathb = findpath("gold", caveb);
        printanswer("caveb = " , caveb);
        printanswer("pathb = " , pathb);
        printanswer("follow = " , follow(pathb, caveb));

        Cons treea = list(list("my", "eyes"),
                          list("have", "seen", list("the", "light")));
        Cons treeb = list(list("my", "ears"),
                          list("have", "heard", list("the", "music")));
        printanswer("treea = " , treea);
        printanswer("treeb = " , treeb);
        printanswer("corresp = " , corresp("light", treea, treeb));
        System.out.println("formulas = ");
        Cons frm = formulas;
        Cons vset = null;
        while ( frm != null ) {
            printanswer("   "  , ((Cons)first(frm)));
            vset = vars((Cons)first(frm));
            while ( vset != null ) {
                printanswer("       "  ,
                    solve((Cons)first(frm), (String)first(vset)) );
                vset = rest(vset); }
            frm = rest(frm); }

        Cons bindings = list( list("a", (Double) 32.0),
                              list("t", (Double) 4.0));
        printanswer("Eval:      " , rhs((Cons)first(formulas)));
        printanswer("  bindings " , bindings);
        printanswer("  result = " , eval(rhs((Cons)first(formulas)), bindings));

        printanswer("Tower: " , solveit(formulas, "h0",
                                            list(list("h", new Double(0.0)),
                                                 list("t", new Double(4.0)))));

        printanswer("Car: " , solveit(formulas, "a",
                                            list(list("v", new Double(88.0)),
                                                 list("t", new Double(8.0)))));
        
        printanswer("Capacitor: " , solveit(formulas, "c",
                                            list(list("v", new Double(3.0)),
                                                 list("v0", new Double(6.0)),
                                                 list("r", new Double(10000.0)),
                                                 list("t", new Double(5.0)))));

        printanswer("Ladder: " , solveit(formulas, "b",
                                            list(list("a", new Double(6.0)),
                                                 list("c", new Double(10.0)))));


      }

}


//similar solve
/*
 *	else if (consp(rhs(e)))
	{
		Cons solvereturn = null;
		
		Object left = lhs(e);
		Cons right = (Cons)rhs(e);
		Object operator = op(right);
		Object NewOperator = second(assoc(operator, opposites));
		

		if (rhs(right)==null)
		{
			if (operator.equals("sqrt"))
			{
				return solve(list ("=", list("expt", left, "2"), lhs(right)), v);
			}
			
			else if (operator.equals("-"))
			{
				return solve(list ("=", list("-",left), lhs(right)), v);
			}

			else
			{
				return solve(list ("=", list(NewOperator, left, lhs(right)), lhs(right)), v);
			}
					
		}
		
		else
		{
			if (operator.equals("expt"))
			{
				return solve()
			}
			else
			{
				
			}
		}
	}
 *
 */





/*
 *public static Cons findpath(Object item, Object cave) {
	
	System.out.println("   " + cave);
	//cons
	if (consp(cave))
	{
		Cons cons_cave = (Cons)cave;
		
		if ((findpath(item, first(cons_cave)))!=null)
		{
			return list("done");
		}
		
		else if ((findpath(item, rest(cons_cave)))!=null)
		{
			return list("done");
		}
		
		else
		{
			return null;
		}
		
	}
	
	//not cons
	else
	{
		if (item.equals(cave))
			return list("done");
		
		return list("not found");
		
	}
 }
 *
 */

/*public static Cons findpath(Object item, Object cave) {
	
	if (item.equals(cave))
	{
		return list("done");
	
	}
	else if (consp(cave))
	{
		Cons conscave = (Cons)cave;
		
		if (!consp(cave))
		{
			if (first(conscave).equals(item))
			{
				return list("done");
			}
			else
			{
				return findpath(item, rest(conscave));
			}			
		}
		else
		{
			
		}

	}
	else
	{
		return null;
	}
 }
 *
 */
 
 
 // partial copy for green book
 /*
  *public static Cons findpath(Object item, Object cave) {
	
	if (item==null)
	{
		return null;
	}
	else if ()
	else if (consp(cave))
	{
		Cons cons_cave = (Cons)cave;
		
		if (item.equals(first(cons_cave)))
			return cons_cave;
		else
			return findpath(item, rest(cons_cave));
	}
 }
  */