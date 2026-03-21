import java.io.IOException;
import java.util.Scanner;

public class Main {

    public void chamaOrdenacao(int op) {
        Arquivo aux = new Arquivo();
        Arquivo arquivoOrdenado  = new Arquivo("Ordenado.dat");
        Arquivo arquivoAleatorio = new Arquivo("Aleatorio.dat");
        Arquivo arquivoReverso   = new Arquivo("Reverso.dat");
        aux.carregaOrdenacao(op, arquivoOrdenado, arquivoAleatorio, arquivoReverso);
    }

    public void menu() throws IOException {
        int opcao, opcaoRunning = 90;
        Scanner input = new Scanner(System.in);

        while (opcaoRunning != 0) {
            System.out.println("PAINEL - ORDENACAO EM ARQUIVO");
            System.out.println("|1|  Executar todos e gerar tabela");
            System.out.println("|2|  Testar ordenacao individual");
            System.out.println("|0|  Sair");

            opcao = input.nextInt();
            opcaoRunning = opcao;

            if (opcao == 1) {
                Tabela tabela = new Tabela();
                tabela.gerarTabela();

            } else if (opcao == 2) {
                System.out.println("|1|  Insercao Direta");
                System.out.println("|2|  Insercao Binaria");
                System.out.println("|3|  Selecao Direta");
                System.out.println("|4|  Bolha");
                System.out.println("|5|  Shake");
                System.out.println("|6|  Shell Sort");
                System.out.println("|7|  Heap Sort");
                System.out.println("|8|  Quick c/ Pivo");
                System.out.println("|9|  Quick s/ Pivo");
                System.out.println("|10| Merge Sort 1a Impl");
                System.out.println("|11| Merge Sort 2a Impl");
                System.out.println("|12| Counting Sort");
                System.out.println("|13| Bucket Sort");
                System.out.println("|14| Radix Sort");
                System.out.println("|15| Comb Sort");
                System.out.println("|16| Gnome Sort");
                System.out.println("|17| Tim Sort");

                opcao = input.nextInt();
                chamaOrdenacao(opcao);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Main m = new Main();
        m.menu();
    }

}