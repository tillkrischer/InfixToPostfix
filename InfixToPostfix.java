/* This class asks the user for an input in infix notation, then standardizes the input
 * then converts it to postfix and then evaluates the expression
 * 
 * Author:	Trey Yetter, Till Krischer
 * Course:	CS221
 * Assignment:	Project 2
 * Date:	10/9/2014
 */

import java.util.Scanner;

public class InfixToPostfix {
		
	/**
	 * Reads in an Operand from an input String starting at index i and put them into a Queue of Terms
	 * trys to recursively call readOperator or itself depending on the operator
	 *
	 * @param input		input as String
	 * @param i 		index to start reading from
	 * @param terms 	Queue to put terms in
	 */
	public static void readOperand(String input, int i, ArrayQueue<Term> terms) throws InvalidOperandException, MissingOperandException, InvalidOperatorException {
		//if end of string is not reached
		if(i < input.length()) {
			//read in char at index
			char c = input.charAt(i);
			if(c == ' ')
				//if its empty go one character ahead
				readOperand(input, i + 1, terms);
			else if(c == '(') {
				//if its a left parenthesis enqueue it as a new Operator and look for another operand
				terms.enqueue(new Operator('('));
				readOperand(input, i + 1, terms);
			} else if(Character.isDigit(c) || c == '+' || c == '-' || c == '.') {
				//if it starts like a double try reading in a double
				String operand = "";
				double value;
				operand += c;
				i++;
				//read following character into a string until the end or a character that cannot be in a double is reached
				while(i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
					operand += input.charAt(i);
					i++;
				}
				//try to convert the string to a double, throw error if not possible
				try {
					value = Double.parseDouble(operand);
				} catch (NumberFormatException e) {
					throw new InvalidOperandException();
				}
				//enqueue new Operand and look for a operator
				terms.enqueue(new Operand(value));
				readOperator(input, i, terms);
			//if character cannot be in a double throw exception
			} else
				throw new InvalidOperandException();
		//if end of string is reached without finding an operand throw error
		} else
			throw new MissingOperandException();
	}

	/**
	 * Reads in an Operator from an input String starting at index i and put them into a Queue of Terms
	 * trys to recursively call readOperand or itself depending on the operator
	 *
	 * @param input		input as String
	 * @param i 		index to start reading from
	 * @param terms 	Queue to put terms in
	 */
	private static void readOperator(String input, int i, ArrayQueue<Term> terms) throws InvalidOperandException, MissingOperandException, InvalidOperatorException {
		//only read if there are characters letf
		if(i < input.length()) {
			char c = input.charAt(i);
			switch(c) {
			//if character is space go on to the next character
			case ' ':
				readOperator(input, i + 1, terms);
				break;
			//if character is a normal operator enqueue it then look for an operand
			case '+': case '-': case '*': case '/':
				terms.enqueue(new Operator(c));
				readOperand(input, i + 1, terms);
				break;
			//if character is right parenthesis enqueue it and look for an operator
			case ')':
				terms.enqueue(new Operator(c));
				readOperator(input, i + 1, terms);
				break;
			//if character is not a known operand throw error
			default:
				throw new InvalidOperatorException(c);
			}
		}
	}
	
	/**
	 * Takes a Queue of terms in infix notation and returns a Queue in postfix notation
	 *
	 * @param 	infix 	Queue containing infix notation
	 * 
	 * @return	returns a Queue containing the postfix notation
	 */
	private static ArrayQueue<Term> convertToPostfix(ArrayQueue<Term> infix) throws UnbalancedLeftParenthesisException, UnbalancedRightParenthesisException {
		ArrayQueue<Term> postfix = new ArrayQueue<Term>();
		ArrayStack<Operator> operators = new ArrayStack<Operator>();
		
		while(! infix.isEmpty()) {
			//if term is an operand add it to output queue
			if(infix.front() instanceof Operand)
				postfix.enqueue(infix.dequeue());
			else if(infix.front() instanceof Operator) {
				Operator temp = (Operator) infix.dequeue();
				//if term is a left parenthesis push it on operators stack
				if(temp.getType() == '(')
					operators.push(temp);
				else if(temp.getType() == ')') {
					//pop everything of until next left parenthesis
					while(! operators.isEmpty() && ! (operators.peek().getType() == '('))
						postfix.enqueue(operators.pop());
					//if no left parenthesis found throw error
					if(operators.isEmpty())
						throw new UnbalancedRightParenthesisException();
					//pop of left parenthesis
					operators.pop();
				//otherwise the term will be a regular operator (not a parenthesis) 
				} else {
					//if operatorstack is empty add operator to stack
					if(operators.isEmpty())
						operators.push(temp);
					else {
						//pop operators with equal or lower precedence from the stack
						//then push current operator onto the stack
						while(!operators.isEmpty() && temp.comparePrecedence(operators.peek()) <= 0)
							postfix.enqueue(operators.pop());
						operators.push(temp);
					}
				}
			}
		}
		//pop rest of the operators
		while(! operators.isEmpty()) {
			//if there is a unmatched left parenthesis on the stack throw error
			if(operators.peek().getType() == '(')
				throw new UnbalancedLeftParenthesisException();
			postfix.enqueue(operators.pop());
		}
		return postfix;
	}
	
	/**
	 * Evaluates a postfix expression and returns its result
	 *
	 * @param 	postfix 	Queue containing postfix notation
	 * 
	 * @return	returns the result as a double
	 */
	public static double evaluatePostfix(ArrayQueue<Term> postfix) {
		ArrayStack<Operand> stack = new ArrayStack<Operand>();
		//loop through the queue
		while(! postfix.isEmpty()) {
			//if term is an operand put it on the stack
			if(postfix.front() instanceof Operand)
				stack.push((Operand)postfix.dequeue());
			//if its an operator, pop two operands of the operand stack and
			// and perform right operation, then push result back on the stack
			else if(postfix.front() instanceof Operator) {
				double a = stack.pop().getValue();
				double b = stack.pop().getValue();
				switch(((Operator)postfix.dequeue()).getType()) {
				case '+':
					stack.push(new Operand(a + b));
					break;
				case '-':
					stack.push(new Operand(b - a));
					break;
				case '*':
					stack.push(new Operand(a * b));
					break;
				case '/':
					stack.push(new Operand(b / a));
					break;
				}
			}		
		}
		//return last remaining operand on the stack
		return stack.pop().getValue();
	}

	/**
	 * Main method, handels userinput and errors
	 */
	public static void main(String[] args) {
		Scanner n = new Scanner(System.in);
		String input;
		ArrayQueue<Term> postfix, infix = new ArrayQueue<Term>();
		
		System.out.print("Enter infix expression: ");
		input = n.nextLine();
		n.close();
		
		try {
			readOperand(input, 0, infix);
			System.out.println("Standardized infix: " + infix);
			postfix = convertToPostfix(infix);
			System.out.println("Postfix expression: " + postfix);
			System.out.println("Answer: " + evaluatePostfix(postfix));
		//print out right error messege for different exceptions
		} catch(InvalidOperandException e) {
			System.out.println("Invalid operand");
		} catch(MissingOperandException e) {
			System.out.println("Missing operand");
		} catch(InvalidOperatorException e) {
			System.out.println("Invalid operator: " + e.getOperator());
		} catch (UnbalancedLeftParenthesisException e) {
			System.out.println("Unbalanced left parenthesis '('");
		} catch (UnbalancedRightParenthesisException e) {
			System.out.println("Unbalanced right parenthesis ')'");
		}
	}
}
