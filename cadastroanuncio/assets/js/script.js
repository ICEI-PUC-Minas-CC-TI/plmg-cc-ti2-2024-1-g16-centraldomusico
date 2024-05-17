function enviarDados() {
  var casaEvento = document.getElementById('caixaTexto').value;
  var descricao = document.getElementById('descricao').value;
  var data = document.getElementById('data').value;

  // Criar um objeto com os dados
  var anuncio = {
    casaEvento: casaEvento,
    descricao: descricao,
    data: data
  };

  // Configurar as opções para a requisição fetch
  var options = {
    method: 'POST', // ou 'PUT', 'GET', etc., dependendo do que você precisa
    headers: {
      'Content-Type': 'application/json' // Indica que estamos enviando JSON
      // Adicione outros cabeçalhos conforme necessário
    },
    body: JSON.stringify(anuncio) // Converte o objeto para uma string JSON
  };

  // Substitua 'http://exemplo.com/api' pelo URL real do seu servidor
  var url = 'https://jsonservercentraldomusico.arthurcarvalh19.repl.co/anuncio';

  // Realizar a requisição fetch
  fetch(url, options)
    .then(response => {
      if (!response.ok) {
        throw new Error('Erro ao enviar os dados');
      }
      return response.json();
    })
    .then(data => {
      // Manipular a resposta do servidor, se necessário
      console.log('Resposta do servidor:', data);
      alert("Cadastro realizado com sucesso!!!");
    })
    .catch(error => {
      console.error('Erro durante a requisição fetch:', error);
      alert("Erro ao enviar os dados. Tente novamente.");
    });
}
