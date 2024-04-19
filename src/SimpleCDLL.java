import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Simple doubly-linked lists.
 *
 * These do *not* (yet) support the Fail Fast policy.
 */
public class SimpleCDLL<T> implements SimpleList<T> {
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The front of the list
   */
  Node2<T> front;

  /**
   * The number of values in the list.
   */
  int size;

  /**
   * Fail case
   */
  static int actions = 0;
  int individualActions;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a list with a single dummy node.
   */
  public SimpleCDLL() {
    Node2<T> dummyNode = new Node2<T>(null);
    dummyNode.next = dummyNode;
    this.front = dummyNode;
    this.size = 0;
    individualActions = actions;
  } // SimpleDLL

  // +-----------+---------------------------------------------------------
  // | Iterators |
  // +-----------+

  public Iterator<T> iterator() {
    return listIterator();
  } // iterator()

  public ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      // +--------+--------------------------------------------------------
      // | Fields |
      // +--------+

      /**
       * The position in the list of the next value to be returned.
       * Included because ListIterators must provide nextIndex and
       * prevIndex.
       */
      int pos = 0;

      /**
       * The cursor is between neighboring values, so we start links
       * to the previous and next value..
       */
      Node2<T> prev = SimpleCDLL.this.front;
      Node2<T> next = SimpleCDLL.this.front;

      /**
       * The node to be updated by remove or set.  Has a value of
       * null when there is no such value.
       */
      Node2<T> update = null;

      // +---------+-------------------------------------------------------
      // | Methods |
      // +---------+

      public void add(T val) throws UnsupportedOperationException {

        if (actions != individualActions) {
          System.out.println("Iterator Invalidated");
          return;
        }

        this.prev = this.prev.insertAfter(val);
        // Note that we cannot update
        this.update = null;

        // Increase the size
        ++SimpleCDLL.this.size;

        // Update the position.  (See SimpleArrayList.java for more of
        // an explanation.)
        ++this.pos;

        actions++;
        individualActions++;
      } // add(T)

      public boolean hasNext() {
        return (this.pos < SimpleCDLL.this.size);
      } // hasNext()

      public boolean hasPrevious() {
        return (this.pos > 0);
      } // hasPrevious()

      public T next() {
        if (!this.hasNext()) {
         throw new NoSuchElementException();
        } // if
        // Identify the node to update
        this.update = this.next;
        // Advance the cursor
        this.prev = this.next;
        this.next = this.next.next;
        // Note the movement
        ++this.pos;
        // And return the value
        return this.update.value;
      } // next()

      public int nextIndex() {
        return this.pos;
      } // nextIndex()

      public int previousIndex() {
        return this.pos - 1;
      } // prevIndex

      public T previous() throws NoSuchElementException {
        if (!this.hasPrevious()) {
          throw new NoSuchElementException();
        } // if
        // Identify the node to update
        this.update = this.prev;
        // Advance the cursor
        this.prev = this.prev.prev;
        this.next = this.prev;
        // Note the movement
        --this.pos;
        // And return the value
        return this.update.value;
      } // previous()

      public void remove() {

        if (actions != individualActions) {
          System.out.println("Iterator Invalidated");
          return;
        }

        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if

        // Update the cursor
        if (this.next == this.update) {
          this.next = this.update.next;
        } // if
        if (this.prev == this.update) {
          this.prev = this.update.prev;
          --this.pos;
        } // if

        // Update the front
        if (SimpleCDLL.this.front == this.update) {
          SimpleCDLL.this.front = this.update.next;
        } // if

        // Do the real work
        this.update.remove();
        --SimpleCDLL.this.size;

        actions++;
        individualActions++;

        // Note that no more updates are possible
        this.update = null;
      } // remove()

      public void set(T val) {
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if
        // Do the real work
        this.update.value = val;
        // Note that no more updates are possible
        this.update = null;
      } // set(T)
    };
  } // listIterator()

} // class SimpleCDLL<T>
