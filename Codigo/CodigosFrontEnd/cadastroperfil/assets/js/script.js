function enviarDados() {
    var nome = document.getElementById('caixaTextoge').value;
    var descricao = document.getElementById('descricao').value;
    var usuario = document.getElementById('caixaTextoge').value;
    var senha = document.getElementById('senha').value;
    var confirmSenha = document.getElementById('confirmSenha').value;
    var instrumento1 = document.getElementById('instrumento1').value;
    var instrumento2 = document.getElementById('instrumento2').value;
    var instrumento3 = document.getElementById('instrumento3').value;
    var cache = document.getElementById('cache').value;
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
        console.log('Resposta do servidor:', data);
    })
    .catch(error => {
        console.error('Erro durante a requisição fetch:', error);
    });
}
