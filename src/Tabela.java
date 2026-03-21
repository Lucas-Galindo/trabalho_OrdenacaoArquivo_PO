import java.io.*;

public class Tabela {

    public FileWriter iniciaTabela() throws IOException {
        FileWriter arq = new FileWriter("Tabela1.txt");
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf("__________________________________________________________________________________________________________________________________________________________________________________________________________________________%n");
        gravarArq.printf("|Métodos Ordenação | \t\t\tArquivo Ordenado\t\t\t | \t\t\tArquivo em Ordem Reversa\t\t\t | \t\t\tArquivo Randômico\t\t\t |%n");
        gravarArq.printf("_________________________________________________________________________________________________________________________________________________________________________________________________________________________|%n");
        gravarArq.printf("\t\t | Comp. Prog. *| Comp. Equa. #| Mov. Prog. +| Mov. Equa. -| Tempo(ms)| Comp. Prog. *| Comp. Equa. #| Mov. Prog. +| Mov. Equa. -| Tempo(ms)| Comp. Prog. *| Comp. Equa. #| Mov. Prog. +| Mov. Equa. -| Tempo(ms)|%n");
        gravarArq.printf("_________________________________________________________________________________________________________________________________________________________________________________________________________________________|%n");
        gravarArq.flush();
        return arq;
    }

    private void gravaLinhaTabela(FileWriter arq, String metodo,
                                  long compOrd, long compRev, long compRand,
                                  long movOrd, long movRev, long movRand,
                                  double compEquaOrd, double compEquaRev, double compEquaRand,
                                  double movEquaOrd, double movEquaRev, double movEquaRand,
                                  long ttotalOrd, long ttotalRev, long ttotalRand) throws IOException {
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf("" + metodo + "    |\t" + String.format("%-6.6s", compOrd) + "\t  |\t" + String.format("%-6.6s", compEquaOrd) + "\t |      " + String.format("%-6.6s", movOrd) + " |      " + String.format("%-2.6s", movEquaOrd) + " |\t" + String.format("%-2.6s", ttotalOrd) + "   |\t    " + String.format("%-6.6s", compRev) + "  |\t  " + String.format("%-6.6s", compEquaRev) + "   |\t " + String.format("%-6.6s", movRev) + "  | \t" + String.format("%-6.6s", movEquaRev) + " |  " + String.format("%-2.6s", ttotalRev) + "   |     " + String.format("%-6.6s", compRand) + "   |\t  " + String.format("%-6.6s", compEquaRand) + "     |\t    " + String.format("%-6.6s", movRand) + " |\t " + String.format("%-6.6s", movEquaRand) + "  |  " + String.format("%-4.6s", ttotalRand) + " |%n");
        gravarArq.printf("_________________________________________________________________________________________________________________________________________________________________________________________________________________________|%n");
        gravarArq.flush();
    }

    public void exibirTabela() throws IOException {
        FileInputStream stream = new FileInputStream("Tabela1.txt");
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);
        String linha = br.readLine();
        while (linha != null) {
            System.out.println(linha);
            linha = br.readLine();
        }
        br.close();
    }

    public void gerarTabela() throws IOException {
        Arquivo arqOrd  = new Arquivo("Ordenado.dat");
        Arquivo arqRev  = new Arquivo("Reverso.dat");
        Arquivo arqRand = new Arquivo("Aleatorio.dat");
        Arquivo auxOrd  = new Arquivo("auxOrdenado.dat");
        Arquivo auxRev  = new Arquivo("auxReverso.dat");
        Arquivo auxRand = new Arquivo("auxAleatorio.dat");

        arqOrd.geraOrdenado();
        arqRev.geraReverso();
        arqRand.geraRandom();

        long n = arqOrd.filesize();
        long inicio, fim, tOrd, tRev, tRand;

        FileWriter arq = iniciaTabela();

        // --- Insercao Direta ---
        System.out.println("Insercao Direta...");
        arqOrd.copiaArquivo(auxOrd);   auxOrd.zeraComp();  auxOrd.zeraMov();
        inicio = System.currentTimeMillis(); auxOrd.insercaoDireta();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);   auxRev.zeraComp();  auxRev.zeraMov();
        inicio = System.currentTimeMillis(); auxRev.insercaoDireta();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand); auxRand.zeraComp(); auxRand.zeraMov();
        inicio = System.currentTimeMillis(); auxRand.insercaoDireta(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        gravaLinhaTabela(arq, "Insercao Direta ",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                (double)(n - 1),
                (double)(n * n + n - 4) / 4,
                (double)(n * n + n - 2) / 4,
                3.0 * (n - 1),
                (double)(n * n + 3 * n - 4) / 2,
                (double)(n * n + 9 * n - 10) / 4,
                tOrd, tRev, tRand);

        // --- Insercao Binaria ---
        System.out.println("Insercao Binaria...");
        arqOrd.copiaArquivo(auxOrd);   auxOrd.zeraComp();  auxOrd.zeraMov();
        inicio = System.currentTimeMillis(); auxOrd.insercaoBinaria();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);   auxRev.zeraComp();  auxRev.zeraMov();
        inicio = System.currentTimeMillis(); auxRev.insercaoBinaria();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand); auxRand.zeraComp(); auxRand.zeraMov();
        inicio = System.currentTimeMillis(); auxRand.insercaoBinaria(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        double logN = Math.log10(n) - Math.log(Math.E) + 0.5;
        gravaLinhaTabela(arq, "Insercao Binaria",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                n * logN, n * logN, n * logN,
                3.0 * (n - 1),
                (double)(n * n + 3 * n - 4) / 2,
                (double)(n * n + 9 * n - 10) / 4,
                tOrd, tRev, tRand);

        // --- Selecao Direta ---
        System.out.println("Selecao Direta...");
        arqOrd.copiaArquivo(auxOrd);   auxOrd.zeraComp();  auxOrd.zeraMov();
        inicio = System.currentTimeMillis(); auxOrd.selecaoDireta();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);   auxRev.zeraComp();  auxRev.zeraMov();
        inicio = System.currentTimeMillis(); auxRev.selecaoDireta();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand); auxRand.zeraComp(); auxRand.zeraMov();
        inicio = System.currentTimeMillis(); auxRand.selecaoDireta(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        gravaLinhaTabela(arq, "Selecao Direta  ",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                (double)(n * n - n) / 2,
                (double)(n * n - n) / 2,
                (double)(n * n - n) / 2,
                3.0 * (n - 1),
                (double)(n * n + 3 * n - 4) / 2,
                n * (Math.log10(n) + 0.577216),
                tOrd, tRev, tRand);

        // --- Bolha ---
        System.out.println("Bolha...");
        arqOrd.copiaArquivo(auxOrd);   auxOrd.zeraComp();  auxOrd.zeraMov();
        inicio = System.currentTimeMillis(); auxOrd.bolha();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);   auxRev.zeraComp();  auxRev.zeraMov();
        inicio = System.currentTimeMillis(); auxRev.bolha();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand); auxRand.zeraComp(); auxRand.zeraMov();
        inicio = System.currentTimeMillis(); auxRand.bolha(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        gravaLinhaTabela(arq, "Bolha           ",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                (double)(n - 1),
                (double)(n * n - n) / 2,
                (double)(n * n - n) / 4,
                0, (double) 3 * n * (n - 1) / 2, (double) 3 * n * (n - 1) / 4,
                tOrd, tRev, tRand);

        // --- Shake ---
        System.out.println("Shake...");
        arqOrd.copiaArquivo(auxOrd);   auxOrd.zeraComp();  auxOrd.zeraMov();
        inicio = System.currentTimeMillis(); auxOrd.shake();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);   auxRev.zeraComp();  auxRev.zeraMov();
        inicio = System.currentTimeMillis(); auxRev.shake();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand); auxRand.zeraComp(); auxRand.zeraMov();
        inicio = System.currentTimeMillis(); auxRand.shake(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        gravaLinhaTabela(arq, "Shake           ",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                0, 0, 0, 0, 0, 0,
                tOrd, tRev, tRand);

        // --- Heap Sort ---
        System.out.println("Heap Sort...");
        arqOrd.copiaArquivo(auxOrd);   auxOrd.zeraComp();  auxOrd.zeraMov();
        inicio = System.currentTimeMillis(); auxOrd.heapSort();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);   auxRev.zeraComp();  auxRev.zeraMov();
        inicio = System.currentTimeMillis(); auxRev.heapSort();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand); auxRand.zeraComp(); auxRand.zeraMov();
        inicio = System.currentTimeMillis(); auxRand.heapSort(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        gravaLinhaTabela(arq, "Heap Sort       ",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                0, 0, 0, 0, 0, 0,
                tOrd, tRev, tRand);

        // --- Shell Sort ---
        System.out.println("Shell Sort...");
        arqOrd.copiaArquivo(auxOrd);   auxOrd.zeraComp();  auxOrd.zeraMov();
        inicio = System.currentTimeMillis(); auxOrd.shellSort();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);   auxRev.zeraComp();  auxRev.zeraMov();
        inicio = System.currentTimeMillis(); auxRev.shellSort();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand); auxRand.zeraComp(); auxRand.zeraMov();
        inicio = System.currentTimeMillis(); auxRand.shellSort(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        gravaLinhaTabela(arq, "Shell Sort      ",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                0, 0, 0, 0, 0, 0,
                tOrd, tRev, tRand);

        // --- Quick Sort ---
        System.out.println("Quick Sort...");
        arqOrd.copiaArquivo(auxOrd);
        inicio = System.currentTimeMillis(); auxOrd.quickSort();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);
        inicio = System.currentTimeMillis(); auxRev.quickSort();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand);
        inicio = System.currentTimeMillis(); auxRand.quickSort(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        gravaLinhaTabela(arq, "Quick Sort      ",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                0, 0, 0, 0, 0, 0,
                tOrd, tRev, tRand);

        // --- Merge Sort ---
        System.out.println("Merge Sort...");
        arqOrd.copiaArquivo(auxOrd);
        inicio = System.currentTimeMillis(); auxOrd.mergeSort();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);
        inicio = System.currentTimeMillis(); auxRev.mergeSort();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand);
        inicio = System.currentTimeMillis(); auxRand.mergeSort(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        gravaLinhaTabela(arq, "Merge Sort      ",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                0, 0, 0, 0, 0, 0,
                tOrd, tRev, tRand);

        // --- Counting Sort ---
        System.out.println("Counting Sort...");
        arqOrd.copiaArquivo(auxOrd);   auxOrd.zeraComp();  auxOrd.zeraMov();
        inicio = System.currentTimeMillis(); auxOrd.countingSort();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);   auxRev.zeraComp();  auxRev.zeraMov();
        inicio = System.currentTimeMillis(); auxRev.countingSort();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand); auxRand.zeraComp(); auxRand.zeraMov();
        inicio = System.currentTimeMillis(); auxRand.countingSort(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        gravaLinhaTabela(arq, "Counting Sort   ",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                0, 0, 0, 0, 0, 0,
                tOrd, tRev, tRand);

        // --- Comb Sort ---
        System.out.println("Comb Sort...");
        arqOrd.copiaArquivo(auxOrd);   auxOrd.zeraComp();  auxOrd.zeraMov();
        inicio = System.currentTimeMillis(); auxOrd.combSort();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);   auxRev.zeraComp();  auxRev.zeraMov();
        inicio = System.currentTimeMillis(); auxRev.combSort();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand); auxRand.zeraComp(); auxRand.zeraMov();
        inicio = System.currentTimeMillis(); auxRand.combSort(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        gravaLinhaTabela(arq, "Comb Sort       ",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                0, 0, 0, 0, 0, 0,
                tOrd, tRev, tRand);

        // --- Gnome Sort ---
        System.out.println("Gnome Sort...");
        arqOrd.copiaArquivo(auxOrd);   auxOrd.zeraComp();  auxOrd.zeraMov();
        inicio = System.currentTimeMillis(); auxOrd.gnomeSort();  fim = System.currentTimeMillis(); tOrd = fim - inicio;

        arqRev.copiaArquivo(auxRev);   auxRev.zeraComp();  auxRev.zeraMov();
        inicio = System.currentTimeMillis(); auxRev.gnomeSort();  fim = System.currentTimeMillis(); tRev = fim - inicio;

        arqRand.copiaArquivo(auxRand); auxRand.zeraComp(); auxRand.zeraMov();
        inicio = System.currentTimeMillis(); auxRand.gnomeSort(); fim = System.currentTimeMillis(); tRand = fim - inicio;

        gravaLinhaTabela(arq, "Gnome Sort      ",
                auxOrd.getComparacao(),  auxRev.getComparacao(),  auxRand.getComparacao(),
                auxOrd.getMovimentacao(),auxRev.getMovimentacao(),auxRand.getMovimentacao(),
                0, 0, 0, 0, 0, 0,
                tOrd, tRev, tRand);

        arq.close();
        System.out.println("\nTabela gerada com sucesso!");
        exibirTabela();
    }

}