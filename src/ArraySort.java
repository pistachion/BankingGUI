import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class ArraySort<E> extends ArrayList<E>
{
	private static final long serialVersionUID = 8282164999208540906L;

	public ArraySort ()
    {
        super ();
    }

    public ArraySort (int initialCapacity)
    {
        super (initialCapacity);
    }

    @SuppressWarnings("unchecked")
    public void sort (Comparator<? super E> c)
    {
        E[] elementData = (E[]) this.toArray ();
        this.clear();
        Arrays.sort (elementData, 0, elementData.length, c);
        this.addAll (Arrays.asList (elementData));
    }
}
