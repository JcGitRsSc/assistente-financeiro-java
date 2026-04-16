import java.time.LocalDate;

public class Transacao {
    private LocalDate data;
    private String descricao;
    private double valor;
    private String categoria;

    public Transacao(LocalDate data, String descricao, double valor, String categoria) {
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
    }
    public LocalDate getData() { return data; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
    public String getCategoria() { return categoria; }

    @Override
    public String toString() {
        return  String.format("%s | %-20s | R$ %8.2f | %s", data, descricao, valor, categoria);
    }
    }
