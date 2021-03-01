package com.algamoney.api.exceptionhandler;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//estendi a classe que captura as exceçõe para quando eu preciso responder
@ControllerAdvice//para ele conseguir observar toda a aplicação
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired//com esse objeto eu consigo capturar as mensagens do messages.properties
	private MessageSource messageSource;
	
	//HttpMessageNotReadableException peguei essa exceção na resposta recebida no postman e sobreescrevi o método da classe ResponseEntityExceptionHandler
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		//para capturar a mensagem do messages.properties
		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		//para capturar a mensagem par o desenvolvedor
		String mensagemDesenvolvedor = ex.getCause().toString();
		//eu posso retornar com outro método do ResponseEntityExceptionHandler
		return handleExceptionInternal(ex,new Erro(mensagemUsuario,mensagemDesenvolvedor), headers, HttpStatus.BAD_REQUEST, request);
	}
	
	//como fica estranho concatenar as duas menssagens de retorno no handleExceptionInternal,
	//eu posso criar uma classe com as messages e passar elas como parâmetro
	public static class Erro{
		private String mensagemUsuario;
		private String mensagemDesenvolvedor;
		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
			super();
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}
		public String getMensagemUsuario() {
			return mensagemUsuario;
		}
		
		public String getMensagemDesenvolvedor() {
			return mensagemDesenvolvedor;
		}
		
		
		
	}

}
