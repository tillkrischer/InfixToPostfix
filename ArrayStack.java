import java.util.NoSuchElementException;

public class ArrayStack<T> {
	private T[] data;
	private int size;
	
	public ArrayStack() {
		data = (T[]) new Object[10];
	}
	
	public void push(T element) {
		if(size == data.length) {
			T[] temp = (T[]) new Object[data.length * 2];
			for(int i = 0; i < size; ++i)
				temp[i] = data[i];
			data = temp;
		}
		data[size] = element;
		size++;
	}
	
	public T pop() {
		if(size == 0)
			throw new NoSuchElementException();
		T temp = data[size-1];
		size--;
		return temp;
	}
	
	public T peek() {
		if(size == 0)
			throw new NoSuchElementException();
		return data[size-1];
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}

	public String toString() {
		String s = "";
		for(int i = 0; i < size; ++i)
			s += data[i] + " ";
		return s;
	}
}
