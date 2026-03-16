import java.io.*;

public class Tabela {

    //Tudo que for relacionado a tabela, criacao, exibição e etc
    public FileWriter iniciaTabela() throws IOException {
        FileWriter arq = new FileWriter("Tabela1.txt");

        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf("__________________________________________________________________________________________________________________________________________________________________________________________________________________________%n");
        gravarArq.printf("|Métodos Ordenação | \t\t\tArquivo Ordenado\t\t\t | \t\t\tArquivo em Ordem Reversa\t\t\t | \t\t\tArquivo Randômico\t\t\t |%n");
        gravarArq.printf("_________________________________________________________________________________________________________________________________________________________________________________________________________________________|%n");
        gravarArq.printf("\t\t | Comp. Prog. *| Comp. Equa. #| Mov. Prog. +| Mov. Equa. -| Tempo | Comp. Prog. *| Comp. Equa. #| Mov. Prog. +| Mov. Equa. -| Tempo | Comp. Prog. *| Comp. Equa. #| Mov. Prog. +| Mov. Equa. -| Tempo |%n");
        gravarArq.printf("_________________________________________________________________________________________________________________________________________________________________________________________________________________________|%n");

        return arq;
    }

    private void gravaLinhaTabela(FileWriter arq, String metodo, int compOrd, int compRev, int compRand, int movOrd, int movRev, int movRand, double compEquaOrd, double compEquaRev,
                                  double compEquaRand, double movEquaOrd, double movEquaRev, double movEquaRand,int ttotalOrd, int ttotalRev, int ttotalRand) throws IOException{
        PrintWriter gravarArq = new PrintWriter(arq);

        gravarArq.printf("" + metodo + "    |\t" + String.format("%-6.6s", compOrd) + "\t  |\t" + String.format("%-6.6s", compEquaOrd) + "\t |      " + String.format("%-6.6s", movOrd) + " |      " + String.format("%-2.6s", movEquaOrd) + " |\t" + String.format("%-2.6s", ttotalOrd) + "   |\t    " + String.format("%-6.6s", compRev) + "  |\t  " + String.format("%-6.6s", compEquaRev) + "   |\t " + String.format("%-6.6s", movRev) + "  | \t" + String.format("%-6.6s", movEquaRev) + " |  " + String.format("%-2.6s", ttotalRev) + "   |     " + String.format("%-6.6s", compRand) + "   |\t  " + String.format("%-6.6s", compEquaRand) + "     |\t    " + String.format("%-6.6s", movRand) + " |\t " + String.format("%-6.6s", movEquaRand) + "  |  " + String.format("%-4.6s", ttotalRand) + " |%n");
        gravarArq.printf("_________________________________________________________________________________________________________________________________________________________________________________________________________________________|%n");
    }

    public void exibirTabela() throws FileNotFoundException, IOException{

        FileInputStream stream = new FileInputStream("Tabela1.txt");
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);
        String linha = br.readLine();
        while(linha != null) {
            System.out.println(linha);
            linha = br.readLine();
        }
    }
}
