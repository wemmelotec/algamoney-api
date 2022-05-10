package com.algamoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//estendi a classe que captura as exceçõe para quando eu preciso responder
@ControllerAdvice//para ele conseguir observar toda a aplicação
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired//com esse objeto eu consigo capturar as mensagens do messages.properties
	private MessageSource messageSource;
	
	//HttpMessageNotReadableException peguei essa exceção na resposta recebida no postman e sobreescrevi o método da classe ResponseEntityExceptionHandler
	@Override
	//este método retorna entre outras informações apenas um objeto Erro
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		//para capturar a mensagem do messages.properties
		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		//para capturar a mensagem par o desenvolvedor
		String mensagemDesenvolvedor = ex.getCause()!= null ? ex.getCause().toString() : ex.toString();
		//eu posso retornar com outro método do ResponseEntityExceptionHandler
		return handleExceptionInternal(ex,new Erro(mensagemUsuario,mensagemDesenvolvedor), headers, HttpStatus.BAD_REQUEST, request);
	}
	//MethodArgumentNotValidException peguei essa exceção na log do STS, essa exceção foi lançada pelo Bean Validation do Spring
	@Override
	//esse método retorna dentre outras informações uma lista de erros
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<Erro> erros = criarListaDeErros(ex.getBindingResult());//esse BindingResult é onde tem a lista de todos os erros
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	//criei esse método, que cria uma lista de erro, para o caso de existirem outros atributos a serem validados através do Bean Validation
	//vou utilizar esse método em handleMethodArgumentNotValid
	private List<Erro> criarListaDeErros(BindingResult bindingResult) {//esse parâmetro é uma lista de erros
		List<Erro> erros = new ArrayList<>();
		
		for (FieldError filError : bindingResult.getFieldErrors()) {
			String mensagemUsuario = messageSource.getMessage(filError, LocaleContextHolder.getLocale());
			String mensagemDesenvolvedor = filError.toString();
			erros.add(new Erro(mensagemUsuario,mensagemDesenvolvedor));
		}
		
		return erros;
	}
	
	//esse método, trata a exceção quando eu tento deletar um recurso que não existe no banco
	//esse método eu criei do zero
	@ExceptionHandler({EmptyResultDataAccessException.class})
	private ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,WebRequest request) {
		//lista de erros abaixo
		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario,mensagemDesenvolvedor));
		
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	//Esse método trata a exceção DataIntegrityViolationException, que vem da classe LancamentosResource
    //quando eu tento salvar um lancamento com um código que não existe de categoria ou pessoa
    @ExceptionHandler({DataIntegrityViolationException.class})//Se essa exceção for lançada, ela será recebida aqui.
    //Parametros, a própria exceção DataIntegrityViolationException
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request){
        //criando a lista de erro para o usuário que fez a requisição errada
        String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));

        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
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
