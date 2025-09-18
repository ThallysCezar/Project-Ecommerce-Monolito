package br.com.thallysprojetos.ecommerce.utils;

import br.com.thallysprojetos.ecommerce.dtos.pagamentos.PagamentoDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PagamentoValidator implements ConstraintValidator<ValidacaoPagamento, PagamentoDTO> {

    @Override
    public boolean isValid(PagamentoDTO dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getTipoPagamento() == null) {
            return true;
        }

        return switch (dto.getTipoPagamento()) {
            case CARTAO_CREDITO -> dto.getNomeTitularCartao() != null && !dto.getNomeTitularCartao().isBlank()
                    && dto.getNumeroCartao() != null && !dto.getNumeroCartao().isBlank()
                    && dto.getExpiracaoCartao() != null && !dto.getExpiracaoCartao().isBlank()
                    && dto.getCodigoCartao() != null && !dto.getCodigoCartao().isBlank();
            case BOLETO ->
                    true;
            case PIX -> dto.getChavePix() != null && !dto.getChavePix().isBlank();
        };

    }

}
