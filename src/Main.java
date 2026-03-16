
public void chamaOrdenacao(int op) throws IOException {
    MetodosOrdenacao metodos = new MetodosOrdenacao();

    Arquivo arquivoOrdenado = new Arquivo("Ordenado.dat");
    Arquivo arquivoAleatorio = new Arquivo("Aleatorio.dat");
    Arquivo arquivoReverso = new Arquivo("Reverso.dat");
    metodos.carregaOrdenacao(op,arquivoOrdenado,arquivoAleatorio,arquivoReverso);

}
public void menu() throws IOException {
    int opcao = 90;
    int opcaoRunning = 90;
    while(opcaoRunning!=0)
    {
        System.out.println("PAINEL - ORDENACAO EM ARQUIVO");
        System.out.println("|1| Executar e gerar tabela");
        //System.out.println("|2| Gerar Tabela");
        System.out.println("|2| Testar Ordenacoes");
        System.out.println("|0| Sair");
        Scanner input = new Scanner(System.in);
        opcao = input.nextInt();
        opcaoRunning = opcao;
        if(opcao == 1)
        {
            //Fazer um ciclo while que chama todas as ordenacoes
        }else if(opcao == 2){
            System.out.println("|1| Insercao Direta");
            System.out.println("|2| Insercao Binaria");
            System.out.println("|3| Selecao Direta");
            System.out.println("|4| ...");
            System.out.println("|5| ...");
            System.out.println("|6| ...");
            opcao = input.nextInt();
            chamaOrdenacao(opcao);
        }


    }

}


void main() throws IOException {

    //Arquivo a = new Arquivo("c:\\arquivo.dat");
    //a.executa();
    menu();

}
