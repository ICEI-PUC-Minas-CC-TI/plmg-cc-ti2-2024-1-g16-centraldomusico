document.addEventListener('DOMContentLoaded', function() {

  const token = localStorage.getItem('token');
  if (token) {
      document.getElementById('loginbotao').style.display = 'none';
      console.log('token checado');
  } else {
      window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
  }
});

function enviarDados() {
  var nomeCasa = document.getElementById('caixaTexto');
  var endereco = document.getElementById('endereco');
  var telefone = document.getElementById('telefone');
  var valor = document.getElementById('valor');
  var donoCasa = localStorage.getItem('nome');
  var horario = document.getElementById('horario');

  // Criar um objeto com os dados
  var anuncio = {
      nomeCasa: nomeCasa.value,
      endereco: endereco.value,
      telefone: telefone.value,
      valor: valor.value,
      donoCasa: donoCasa,
      horario: horario.value
  };

  fetch('http://localhost:6789/casa/postarAnuncio', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(anuncio)
})
.then(response => {
    if (!response.ok) {
        throw new Error('Erro na requisição: ' + response.statusText);
    }
    return response.json();
})
.then(data => {
    alert('Anúncio criado com sucesso!');
    window.location.href = '/Codigo/CodigosFrontEnd/informaçãoanuncio/anuncio.html';
    console.log('Resposta do servidor:', data);
})
.catch(error => {
    console.error('Erro durante a requisição fetch:', error);
});
}
