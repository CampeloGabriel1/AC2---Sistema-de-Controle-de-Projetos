package com.example.controleprojetos.exceptions;

public class RegraNegocioException extends RuntimeException {
public RegraNegocioException(String mensagemErro) {
super(mensagemErro);
}
}