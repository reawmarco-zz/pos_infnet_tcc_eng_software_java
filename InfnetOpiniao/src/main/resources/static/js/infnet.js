$(function(){
	
	$('.datePicker1').datetimepicker({
		    dateFormat: 'dd/m/yy',
			timeFormat: 'HH:mm:ss',
			stepHour: 2,
			stepMinute: 10,
			stepSecond: 10
		});
	
	$('.datePicker2').datetimepicker({
	    dateFormat: 'dd/m/yy',
	    altField: "#none"
	});
	
	$('.js-adicionar-questao').on('click',function(event){
		console.log('here');
		
		event.preventDefault();
		var botaoAdicionar = $(event.currentTarget);
		var urlAdicionar = botaoAdicionar.attr('href');
		if(urlAdicionar === '#'){
			urlAdicionar = $('#urlToSubmit').text();
		}
		
		var response = $.ajax({
			url: urlAdicionar,
			type: 'GET'
			
		});
		
		response.done(function(e) {
			var codigoAvaliacao = botaoAdicionar.data('codigo');
			var codigoQuestao = botaoAdicionar.data('id');
			var tipo = $($('[data-roleq=' + codigoQuestao + '] td')[2]).text();
			var texto = $($('[data-roleq=' + codigoQuestao + '] td')[1]).text();
			$('tadd').append($('[data-roleq=' + codigoQuestao + ']'));
			$('[data-roleq=' + codigoQuestao + ']').remove();
			var newLine = buildLineQuestionTable(codigoAvaliacao, codigoQuestao, texto, tipo, 'tbl-vinculadas');
			$('#tbl-vinculadas > tbody:last-child').append(newLine).html();
			buildLineEmptyTable();			
		});
		
		response.fail(function(e) {
			console.log(e);
			alert('Erro ao adicionar questão');
		});
		
	});
	
	$('.js-remover-questao').on('click',function(event){
		
		event.preventDefault();
		var botaoRemover = $(event.currentTarget);
		var urlRemover = botaoRemover.attr('href');
		if(urlRemover === '#'){
			urlRemover = $('#urlToSubmit').text();
		}
		
		var response = $.ajax({
			url: urlRemover,
			type: 'GET'
			
		});
		
		response.done(function(e) {
			
			var codigoAvaliacao = botaoRemover.data('codigo');
			console.log(codigoAvaliacao);
			var codigoQuestao = botaoRemover.data('id');
			console.log(codigoQuestao);
			var tipo = $($('[data-roleeq=' + codigoQuestao + '] td')[2]).text();
			var texto = $($('[data-roleeq=' + codigoQuestao + '] td')[1]).text();
			$('[data-roleeq=' + codigoQuestao + ']').remove();
			var newLine = buildLineQuestionTable(codigoAvaliacao, codigoQuestao, texto, tipo, 'tbl-disponiveis');
			$('#tbl-disponiveis > tbody:last-child').append(newLine).html();
			buildLineEmptyTable();
		});
		
		response.fail(function(e) {
			console.log(e);
			alert('Erro ao remover questão');
		});
		
	});
});

function buildLineQuestionTable(evaluationId, questionId, questionText, questionType, table){
	var icon = 'minus';
	var action = 'remover';
	var role = 'eq';
	if(table === 'tbl-disponiveis'){
		icon = 'plus'; 
		action = 'adicionar';
		role = 'q';
	}
	
	var lineP1 = '<tr data-role' + role + '="' + questionId + '"><td class="text-center">' + questionId;
	var lineP2 = '</td><td>' + questionText + '</td><td class="text-center">' + questionType;
	var lineP3 = '</td><td class="text-center"><a class="btn btn-link btn-xs js-' + action + '-questao" data-codigo="' + evaluationId;
//	var lineP4 = '" data-id="' + questionId + '" href="/evaluations/' + evaluationId + '/' + action + '/' + questionId;
	var lineP4 = '" data-id="' + questionId + '" href="#" onclick="submitTo('+ evaluationId +','+ questionId +',\''+ table +'\');';
	var lineP5 = '"><span class="glyphicon glyphicon glyphicon-' + icon + '"></span></a></td></tr></script>';
	
	var line = lineP1 + lineP2 + lineP3 + lineP4 + lineP5;
	
	return line;
}

function buildLineEmptyTable(){
	//Disponiveis
	var qtsDisponiveis = $('#trem tr').length;
	if(qtsDisponiveis > 0){
		$('#tbl-disponiveis-empty').remove();
	}else{
		var emptyLine = '<tr id="tbl-disponiveis-empty"><td colspan="6">Nenhuma questão encontrada</td></tr>';
		$('#tbl-disponiveis > tbody:last-child').append(emptyLine).html();
	}
	//Vinculadas
	var qtsVinculadas = $('#tadd tr').length;
	if(qtsVinculadas > 0){
		$('#tbl-vinculadas-empty').remove();
	}else{
		var emptyLine = '<tr id="tbl-vinculadas-empty"><td colspan="6">Nenhuma questão encontrada</td></tr>';
		$('#tbl-vinculadas > tbody:last-child').append(emptyLine).html();
	}	
}

function submitTo(evaluationId, questionId, table){
	event.preventDefault();
	var redirect = 'remover';
	var selector = '.js-remover-questao';
	var dataRole = 'data-roleeq';
	if(table === 'tbl-disponiveis'){
		redirect = 'adicionar';
		selector = '.js-adicionar-questao';
		dataRole = 'data-roleq';
	}
	var url = "/evaluations/" + evaluationId + "/" + redirect + "/" + questionId;
	changeTable(evaluationId, questionId, url, selector, table, dataRole, redirect);
}

function changeTable(evaluationId, questionId, url, selector, table, dataRole, redirect){
	if(url === '#'){
		url = $('#urlToSubmit').text();
	}
	
	var response = $.ajax({
		url: url,
		type: 'GET'
		
	});
	
	response.done(function(e) {
		var selectorDataRole = '['+dataRole+'=';
		var tipo = $($(selectorDataRole + questionId + '] td')[2]).text();
		var texto = $($(selectorDataRole + questionId + '] td')[1]).text();
		$(selectorDataRole + questionId + ']').remove();
		if(table === 'tbl-disponiveis'){
			table = 'tbl-vinculadas';
		}else{
			table = 'tbl-disponiveis'
		}
		var newLine = buildLineQuestionTable(evaluationId, questionId, texto, tipo, table);
		var selectorTable = '#' + table + ' >  tbody:last-child';
		$(selectorTable).append(newLine).html();
		buildLineEmptyTable();
	});
	
	response.fail(function(e) {
		console.log(e);
		alert('Erro ao ' + redirect + 'questão');
	});
}

