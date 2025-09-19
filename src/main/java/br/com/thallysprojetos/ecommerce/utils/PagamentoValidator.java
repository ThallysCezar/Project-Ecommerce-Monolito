package br.com.thallysprojetos.ecommerce.utils;

import br.com.thallysprojetos.ecommerce.dtos.PagamentoDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.flywaydb.core.internal.util.StringUtils;

public class PagamentoValidator implements ConstraintValidator<ValidacaoPagamento, PagamentoDTO> {

    @Override
    public boolean isValid(PagamentoDTO dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getTipoPagamento() == null) {
            return true;
        }

        boolean isValid = switch (dto.getTipoPagamento()) {
            case CARTAO_CREDITO -> isCartaoCreditoValido(dto);
            case BOLETO -> isBoletoValido(dto);
            case PIX -> isPixValido(dto);
        };

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Dados de pagamento inválidos para o tipo de pagamento selecionado.").addConstraintViolation();
        }

        return isValid;
    }

    private boolean isCartaoCreditoValido(PagamentoDTO dto) {
        return StringUtils.hasText(dto.getNomeTitularCartao()) &&
                StringUtils.hasText(dto.getNumeroCartao()) &&
                StringUtils.hasText(dto.getExpiracaoCartao()) &&
                StringUtils.hasText(dto.getCodigoCartao());
    }

    private boolean isBoletoValido(PagamentoDTO dto) {
        return true;
    }

    private boolean isPixValido(PagamentoDTO dto) {
        return StringUtils.hasText(dto.getChavePix());
    }

}