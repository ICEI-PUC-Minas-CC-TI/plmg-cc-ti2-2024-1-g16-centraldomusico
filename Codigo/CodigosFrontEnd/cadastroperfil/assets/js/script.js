//funcao para mascarar campo de telefone
function mascaraTelefone(telefone) {
    if (telefone.value.length == 0) {
        telefone.value = '(' + telefone.value;
    }
    if (telefone.value.length == 3) {
        telefone.value = telefone.value + ')';
    }
    if (telefone.value.length == 9) {
        telefone.value = telefone.value + '-';
    }
}
document.getElementById('telefone').addEventListener('input', function(e) {
    //colocar maximo de 11 digitos
    if (e.target.value.length > 15) {
        e.target.value = e.target.value.slice(0, 15);
    }
    let value = e.target.value.replace(/\D/g, ''); // Remove tudo que não é dígito
    value = value.replace(/^(\d{2})(\d)/g, '($1) $2'); // Coloca parênteses em volta dos dois primeiros dígitos
    value = value.replace(/(\d)(\d{4})$/, '$1-$2'); // Coloca hífen entre o quarto e o quinto dígitos
    e.target.value = value;
});


function enviarDados() {
    var nome = document.getElementById('caixaTextoge').value;
    var descricao = document.getElementById('descricao').value;
    var usuario = document.getElementById('caixaTextoge').value;
    var senha = document.getElementById('senha').value;
    var confirmSenha = document.getElementById('confirmSenha').value;
    var instrumento1 = document.getElementById('instrumento1').value;
    var instrumento2 = document.getElementById('instrumento2').value;
    var instrumento3 = document.getElementById('instrumento3').value;
    var cache = document.getElementById('cache').value.replace('R$', '').replace('.', '').replace(',', '.');
    var objetivo = document.getElementById('objetivo').value;
    var estilo = document.getElementById('estilo').value;
    var telefone = document.getElementById('telefone').value;
    var fotoPerfil = document.getElementById('fotoPerfil').files[0]; // Obter o arquivo de imagem
    //printar o tipo do arquivo fotoPerfil
    console.log(fotoPerfil.type);
    //printar imagem
    console.log(fotoPerfil);
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

    var formData = new FormData();
    formData.append('nome', nome);
    formData.append('descricao', descricao);
    formData.append('senha', senha);
    formData.append('cache', parseFloat(cache)); // Ensure cache is a float
    formData.append('instrumento1', instrumento1);
    formData.append('instrumento2', instrumento2);
    formData.append('instrumento3', instrumento3);
    formData.append('objetivo', objetivo);
    formData.append('estilo', estilo);
    formData.append('fotoPerfil', fotoPerfil); // Adicionar a imagem ao FormData
    formData.append('telefone', telefone);
    //printar itens do formData
    for (var pair of formData.entries()) {
        console.log(pair[0] + ', ' + pair[1]);
    }
    fetch('http://localhost:6789/usuario/insert', {
            method: 'POST',
            body: formData
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
            alert('O usuário já existe. Por favor, tente outro nome de usuário.');
            console.error('Erro durante a requisição fetch:', error);
            //printar erro
            console.log(error);

        });
}

document.getElementById('cache').addEventListener('input', function(e) {
    let value = e.target.value.replace(/\D/g, ''); // Remove tudo que não é dígito
    value = (value / 100).toFixed(2); // Divide por 100 para ter duas casas decimais
    value = value.replace('.', ','); // Substitui ponto por vírgula
    value = value.replace(/\B(?=(\d{3})+(?!\d))/g, '.'); // Insere pontos a cada 3 dígitos
    e.target.value = 'R$' + value;
});

function previewImage(event) {
    const preview = document.getElementById('preview');
    const reader = new FileReader();
    reader.onload = function() {
        if (reader.readyState == 2) {
            preview.src = reader.result;
            preview.style.display = 'block';
        }
    }
    reader.readAsDataURL(event.target.files[0]);
}
