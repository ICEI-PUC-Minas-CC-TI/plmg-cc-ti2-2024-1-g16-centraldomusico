var perfil;
function enviarDados() {
  
    var nome = document.getElementById('caixaTexto').value;
    var descricao = document.getElementById('descricao').value;
    var usuario = document.getElementById('caixaTextoge').value;
    var senha = document.getElementById('senha').value;
    var confirmSenha = document.getElementById('confirmSenha').value
    var instrumento1 = document.getElementById('intrumento1').value;
    var instrumento2 = document.getElementById('intrumento2').value;
    var instrumento3 = document.getElementById('intrumento3').value;
    var telefone = document.getElementById('telefone').value;
    var instagram = document.getElementById('instagram').value;

    // Criar um objeto com os dados

    if (nome === "" || usuario === "" || senha === "" || confirmSenha === "" || descricao === "" || instrumento1 === "" || instrumento2 === "" || instrumento3 === "" || telefone === "" || instagram === "") {
      alert("Por favor, preencha todos os campos.");
      return;
  }
  if (senha !== confirmSenha) {
    alert("As senhas não coincidem.");
    return;
}

  // Verificar se a senha tem pelo menos 8 caracteres
  if (senha.length < 8) {
      alert("A senha deve ter no mínimo 8 caracteres.");
      return;
  }

   perfil = {
    nome: nome,
    usuario: usuario,
    senha: senha,
    descricao: descricao,
    telefone: telefone,
    instrumento1: instrumento1,
    instrumento2: instrumento2,
    instrumento3: instrumento3,
    instagram: instagram,
  };
  
  // Aqui você pode adicionar a lógica para enviar os dados se todas as verificações passarem
  alert("Dados enviados com sucesso!");

  console.log('Resposta do servidor:', perfil);

  // Configurar as opções para a requisição fetch
  var options = {
    method: 'POST', // ou 'PUT', 'GET', etc., dependendo do que você precisa
    headers: {
      'Content-Type': 'application/json' // Indica que estamos enviando JSON
      // Adicione outros cabeçalhos conforme necessário
    },
    body: JSON.stringify(perfil) // Converte o objeto para uma string JSON
  };

  // Substitua 'http://exemplo.com/api' pelo URL real do seu servidor
  var url = 'https://jsonservercentraldomusico.arthurcarvalh19.repl.co/perfil';

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
      alert("Perfil criado com sucesso!!!");
      window.location.href = "/explorar/explorar.html";
    })
    .catch(error => {
      console.error('Erro durante a requisição fetch:', error);
      alert("Erro ao enviar os dados. Tente novamente.");
    });
}

    

    
  
  