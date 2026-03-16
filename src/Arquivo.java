import java.io.IOException;
import java.io.RandomAccessFile;

public class Arquivo {
    private String nomearquivo;
    private RandomAccessFile arquivo;
    private int comparacao,movimentacao;

    public RandomAccessFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(RandomAccessFile arquivo) {
        this.arquivo = arquivo;
    }

    public int getComparacao() {
        return comparacao;
    }

    public void setComparacao(int comparacao) {
        this.comparacao = comparacao;
    }

    public int getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(int movimentacao) {
        this.movimentacao = movimentacao;
    }

    public String getNomearquivo() {
        return nomearquivo;
    }

    public void setNomearquivo(String nomearquivo) {
        this.nomearquivo = nomearquivo;
    }

    public Arquivo(String nomearquivo)
    {
        try
        {
            arquivo = new RandomAccessFile("Dados/"+nomearquivo, "rw");
            this.comparacao = this.movimentacao = 0;
        } catch (IOException e)
        { }
    }

    public void executa()
    {
        try {
            leArq();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        exibirArq();
    }

    public void truncate(long pos) //desloca eof
    {
        try
        {
            arquivo.setLength(pos * Registro.length());
        } catch (IOException exc)
        { }
    }

    //semelhante ao feof() da linguagem C
    //verifica se o ponteiro esta no <EOF> do arquivo
    public boolean eof()
    {
        boolean retorno = false;
        try
        {
            if (arquivo.getFilePointer() == arquivo.length())
                retorno = true;
        } catch (IOException e)
        { }
        return (retorno);
    }

    public int filesize() throws IOException {
        return (int) (arquivo.length()/Registro.length());
    }
    //insere um Registro no final do arquivo, passado por par�metro
    public void inserirRegNoFinal(Registro reg) throws IOException {
        seekArq(filesize());//ultimo byte
        reg.gravaNoArq(arquivo);
    }

    public void exibirArq()
    {
        int i;
        Registro aux = new Registro();
        seekArq(0);
        i = 0;
        while (!this.eof())
        {
            System.out.println("Posicao " + i);
            aux.leDoArq(arquivo);
            aux.exibirReg();
            i++;
        }
    }

    public void exibirUmRegistro(int pos)
    {
        Registro aux = new Registro();
        seekArq(pos);
        System.out.println("Posicao " + pos);
        aux.leDoArq(arquivo);
        aux.exibirReg();
    }

    public void seekArq(int pos)
    {
        try
        {
            arquivo.seek(pos * Registro.length());
        } catch (IOException e)
        { }
    }

    public void leArq() throws IOException {
        int codigo, idade;
        String nome;
        codigo = Entrada.leInteger("Digite o codigo");
        while (codigo != 0)
        {
            nome = Entrada.leString("Digite o nome");
            idade = Entrada.leInteger("Digite a idade");
            inserirRegNoFinal(new Registro(codigo, nome, idade));
            codigo = Entrada.leInteger("Digite o c�digo");
        }
    }
    public void zeraComp() {
        this.comparacao = 0;
    }

    public void zeraMov() {
        this.movimentacao = 0;
    }

    public void copiaArquivo(Arquivo auxRev)
    {
        Registro aux = new Registro();
        auxRev.seekArq(0);
        auxRev.truncate(0);
        seekArq(0);
        while (!eof())
        {
            aux.leDoArq(arquivo);
            aux.gravaNoArq(auxRev.arquivo);
        }
    }

    public void geraOrdenado()
    {
        truncate(0);
        seekArq(0);
        Registro reg = new Registro(0,"nome",20);
        for(int i=0;i<1024;i++)
        {
            reg.setCodigo(i+1);
            reg.gravaNoArq(arquivo);
        }
    }
    public void geraReverso()
    {
        truncate(0);
        seekArq(0);
        Registro reg = new Registro(0,"nome",20);
        for(int i=0,j=1023;i<1024;i++,j--)
        {
            reg.setCodigo(j+1);
            reg.gravaNoArq(arquivo);
        }
    }

    public void geraRandom()
    {
        truncate(0);
        seekArq(0);
        Registro reg = new Registro(0,"nome",20);
        for(int i=0;i<1024;i++)
        {
            reg.setCodigo((int) (Math.random() * 1500) + 1);
            reg.gravaNoArq(arquivo);
        }
    }



}
