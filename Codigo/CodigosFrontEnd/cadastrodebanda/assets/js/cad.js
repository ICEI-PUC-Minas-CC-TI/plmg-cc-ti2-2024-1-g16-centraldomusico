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
    var nome = document.getElementById('nomeBanda').value;
    var descricao = document.getElementById('descricao').value;
    var senha = document.getElementById('senha').value;
    //pegar valor de cache sem formatacao ( o valor está em reais)
    var cache = document.getElementById('cache').value.replace('R$', '').replace('.', '').replace(',', '.');
    var dataCriacao = new Date();
    var objetivo = document.getElementById('objetivo').value;
    var estilo = document.getElementById('estilo').value;

    if (nome === "" || estilo === "" || cache === "" || senha === "" || objetivo === "" || descricao === "") {
        alert("Por favor, preencha todos os campos.");
        return;
    }

    if (senha.length < 8) {
        alert("A senha deve ter no mínimo 8 caracteres.");
        return;
    }

    var banda = {
        nome: nome,
        descricao: descricao,
        senha: senha,
        cache: parseFloat(cache), // Ensure cache is a float
        datacriacao: dataCriacao,
        objetivo: objetivo,
        estilo: estilo

    };

    fetch('http://localhost:6789/banda/insert', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(banda)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro na requisição: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            alert('banda criada com sucesso!');
            window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
            console.log('Resposta do servidor:', data);
        })
        .catch(error => {
            console.error('Erro durante a requisição fetch:', error);
        });
}

document.getElementById('cache').addEventListener('input', function(e) {
    let value = e.target.value.replace(/\D/g, ''); // Remove tudo que não é dígito
    value = (value / 100).toFixed(2); // Divide por 100 para ter duas casas decimais
    value = value.replace('.', ','); // Substitui ponto por vírgula
    value = value.replace(/\B(?=(\d{3})+(?!\d))/g, '.'); // Insere pontos a cada 3 dígitos
    e.target.value = 'R$' + value;
});