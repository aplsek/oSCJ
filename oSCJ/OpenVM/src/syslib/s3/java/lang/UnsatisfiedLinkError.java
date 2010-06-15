package java.lang;


public class UnsatisfiedLinkError extends Error
{
  /**
   * Create an exception without a message.
   */
  public UnsatisfiedLinkError()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public UnsatisfiedLinkError(String s)
    {
      super(s);
    }
}
