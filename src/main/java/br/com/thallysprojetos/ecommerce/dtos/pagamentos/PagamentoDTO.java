package br.com.thallysprojetos.ecommerce.dtos.pagamentos;

import br.com.thallysprojetos.ecommerce.models.enums.StatusPagamento;
import br.com.thallysprojetos.ecommerce.models.enums.TipoFormaPagamento;
import br.com.thallysprojetos.ecommerce.utils.ValidacaoPagamento;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidacaoPagamento
public class PagamentoDTO {

    @NotNull
    private Double valor;

    @NotNull
    private TipoFormaPagamento tipoPagamento;

    private StatusPagamento status;

    private String nomeTitularCartao;
    private String numeroCartao;
    private String expiracaoCartao;
    private String codigoCartao;

    private String codigoDeBarrasBoleto;

    private String chavePix;

}