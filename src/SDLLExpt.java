import java.io.PrintWriter;

/**
 * Some simple experiments with SimpleDLLs
 */
public class SDLLExpt {
  public static void main(String[] args) throws Exception {
    PrintWriter pen = new PrintWriter(System.out, true);
    SimpleListExpt.expt1(pen, new SimpleCDLL<String>());
    SimpleListExpt.expt2(pen, new SimpleCDLL<String>());
    SimpleListExpt.expt3(pen, new SimpleCDLL<String>());
    // SimpleListExpt.expt4(pen, new SimpleDLL<String>(), 3);
  } // main(String[]
} // SDLLExpt
