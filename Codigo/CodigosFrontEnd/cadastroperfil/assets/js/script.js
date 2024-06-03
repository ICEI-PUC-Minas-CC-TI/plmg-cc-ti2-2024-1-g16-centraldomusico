
function enviarDados() {
    var nome = document.getElementById('caixaTextoge').value;
    var descricao = document.getElementById('descricao').value;
    var usuario = document.getElementById('caixaTextoge').value;
    var senha = document.getElementById('senha').value;
    var confirmSenha = document.getElementById('confirmSenha').value;
    var instrumento1 = document.getElementById('instrumento1').value;
    var instrumento2 = document.getElementById('instrumento2').value;
    var instrumento3 = document.getElementById('instrumento3').value;
        //pegar valor de cache sem formatacao ( o valor está em reais)
    var cache = document.getElementById('cache').value.replace('R$', '').replace('.', '').replace(',', '.');
    var objetivo = document.getElementById('objetivo').value;
    var estilo = document.getElementById('estilo').value;

    if (usuario === "" || senha === "" || confirmSenha === "" || descricao === "" || instrumento1 === "" || estilo === "" || instrumento2 === "" || instrumento3 === "" || cache === "" || objetivo === "") {
        alert("Por favor, preencha todos os campos.");
        return;
    }
    if (senha !== confirmSenha) {
        alert("As senhas não coincidem.");
        return;
    }

    if (senha.length < 8) {
        alert("A senha deve ter no mínimo 8 caracteres.");
        return;
    }

    var perfil = {
        nome: nome,
        descricao: descricao,
        senha: senha,
        cache: parseFloat(cache), // Ensure cache is a float
        instrumento1: instrumento1,
        instrumento2: instrumento2,
        instrumento3: instrumento3,
        objetivo: objetivo,
        estilo: estilo
    };

    fetch('http://localhost:6789/usuario/insert', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(perfil)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro na requisição: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            alert('Perfil criado com sucesso!');
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