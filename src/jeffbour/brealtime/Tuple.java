package jeffbour.brealtime;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

class Tuple<T>
{
  Tuple(T least, T greatest)
  {
    ordering.addFirst(least);
    ordering.addLast(greatest);
  }

  Tuple(Collection<T> values)
  {
    ordering.addAll(values);
  }

  T least()
  {
    return ordering.getFirst();
  }

  T greatest()
  {
    return ordering.getLast();
  }

  List<T> values()
  {
    return ImmutableList.copyOf(ordering);
  }

  @Override
  public String toString()
  {
    return ordering.toString();
  }

  static <T> Tuple<T> merge(Tuple<T> least, Tuple<T> greatest)
  {
    LinkedList<T> values = new LinkedList<>();
    values.addAll(least.values());
    values.removeLast();
    values.addAll(greatest.values());
    return new Tuple<>(values);
  }

  private final LinkedList<T> ordering = new LinkedList<>();
}
