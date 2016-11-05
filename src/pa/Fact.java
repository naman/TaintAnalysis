package pa;
public class Fact
{
  int func (int x)
  {
    int temp = x;
    temp = 100;
    return temp;
  }
  
  int foo (int play)
  {
    int beta = play;
    int alpha = beta + func(1);
    int gamma = 3;
    if(alpha == 2)
    { 
       return gamma;
    } 
    else
    { 
       gamma = beta + func(beta);
    }
    return gamma;
  }

  public static void main (String[] args)
  {
      int play = Integer.parseInt(args[0]);
      Fact s = new Fact();
      int x = 0;
      int y = 0;
      if(play > 0)
        x = s.foo(play);
      else
      {
        y = s.foo(4);
        System.out.println(y);
      }
      System.out.println(x);
  }
}