document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const id = localStorage.getItem('id');
    const token = localStorage.getItem('token');
    const user = JSON.parse(localStorage.getItem('usuario'));
    if (token) {
        document.getElementById('loginbotao').style.display = 'none';
        loadUserProfile(id, token);
        loadInstrumentos(id,token);
    } else {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
    }
});

//funcao para pegar do backend o nome dos instrumentos do usuario, caso esses nomes batam com os nomes dos instrumentos que ele colocou no frontend, printar Usuario Verificado
function loadInstrumentos(id, token) {
    fetch(`http://localhost:6789/usuario/getNomeInstrumentos?id=${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Resposta do servidor:', data);
        if (data && data.instrumentos) {
            const instrumentosUsuario = JSON.parse(data.instrumentos); // Convertendo a string JSON para array JavaScript
    
            // Pegando os instrumentos do usuário do HTML
            //pegar so o texto do instrumento
            const instrumento1 = localStorage.getItem('instrumento1').toLowerCase();
            const instrumento2 = localStorage.getItem('instrumento2').toLowerCase();
            const instrumento3 = localStorage.getItem('instrumento3').toLowerCase();
            console.log("INSTRUMENTO1");
            console.log(instrumento1);
            console.log("INSTRUMENTOS DO USUARIO");
            console.log(instrumento1, instrumento2, instrumento3);
            console.log("SEPARADOR");
            //deixar todos os instrumentos em minusculo
            console.log("INSTRUMENTOS DO USUARIO");

            instrumentosUsuario.forEach((inst, index) => {
                instrumentosUsuario[index] = inst.toLowerCase();
            });
            console.log(instrumentosUsuario);

            console.log("SEPARADOR");
            // Criando um array com os instrumentos do usuário
            console.log("VERIFICA");
            const verificaInstrumentos = [instrumento1, instrumento2, instrumento3];
            console.log(verificaInstrumentos);
            console.log("SEPARADOR");
            // Verificando se todos os instrumentos do usuário estão presentes na lista recebida
            var usuarioVerificado = false;
            if(instrumento1 == instrumento2 && instrumento2 == instrumento3){
                // Verificar apenas se o instrumento1 está presente na lista do backend
                console.log("SAO IGUAIS");
                usuarioVerificado = instrumentosUsuario.includes(instrumento1.toLowerCase());
                console.log("INSTRUMENTOSUSUARIO "+instrumentosUsuario);
                console.log("INSTRUMENTO1 "+instrumento1);
                //printar usuarioverificado

            }else{
                usuarioVerificado = verificaInstrumentos.every(inst => instrumentosUsuario.includes(inst));
                console.log("NAO SAO IGUAIS");
                console.log("INSTRUMENTOSUSUARIO "+instrumentosUsuario);
                console.log("INSTRUMENTO1 "+instrumento1);
                console.log("INSTRUMENTO2 "+instrumento2);
                console.log("INSTRUMENTO3 "+instrumento3);
                //printar usuarioverificado
                console.log(usuarioVerificado);
            }
            // Criando o card com base na verificação
            const card = document.createElement('div');
            card.classList.add('card');
            //colocar badge de usuario verificado
            //printar os instrumentos do usuario

            console.log(instrumento1, instrumento2, instrumento3);
            console.log("SEPARADOR");
            //printar os instrumentos do backend
            console.log("INSTRUMENTOS DO BACKEND");
            console.log(instrumentosUsuario);
            //se os 3 instrumentos q o usuario colocou no frontend forem IGUAIS, checar so 1 com o backend

                
            
            card.innerHTML = `
                <div class="card-body">
                    <img src="${usuarioVerificado ? '/Codigo/CodigosFrontEnd/perfil/assets/img/verified.png' : '/Codigo/CodigosFrontEnd/perfil/assets/img/not-verified.png'}" class="card-img-top" id="imgver" alt="Verificado">
                </div>
            `;
    
            document.getElementById('verificado').appendChild(card);
        }
    })
    .catch(error => {
        console.error('Erro ao processar requisição:', error);
    });
}



function loadUserProfile(id, token) {
    fetch(`http://localhost:6789/usuario/get/perfil?id=${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Resposta do servidor:', data);
        if (data) { 
            updateUserProfileUI(data);
            localStorage.setItem('nome', data.nome);
            localStorage.setItem('bandaId', data.bandaId);

            // Definir o valor do inputFotoPerfil para a foto de perfil atual, se existir
            if (data.profileImage) {
                const profileImageElem = document.getElementById('fotoPerfil');
                profileImageElem.src = `data:image/png;base64,${data.profileImage}`;
                //colocar classe
                profileImageElem.classList.add('fotodeperfil');
                profileImageElem.style.display = 'block';
                document.getElementById('inputFotoPerfil').src = profileImageElem.src;
            }
            //salvar objeto no local storage
            console.log(data);
            console.log("Telefone: "+data.telefone);
            localStorage.setItem('usuario', JSON.stringify(data));
            //salvar instrumentos no local storage
            localStorage.setItem('instrumento1', data.instrumento1);
            localStorage.setItem('instrumento2', data.instrumento2);
            localStorage.setItem('instrumento3', data.instrumento3);
            //printar objeto do local storage
            console.log(JSON.parse(localStorage.getItem('usuario')));


        }
    })
    .catch(error => {
        console.error('Erro ao buscar dados do usuário:', error);
        document.getElementById('msgError').textContent = 'Erro ao carregar os dados do usuário. Tente novamente.';
    });
}

function updateUserProfileUI(data) {
    document.getElementById('nome').textContent = data.nome;
    document.getElementById('descricao').textContent = data.descricao;
    document.getElementById('cache').textContent = formatCurrency(data.cache);
    document.getElementById('instrumento1').textContent = data.instrumento1;
    document.getElementById('instrumento2').textContent = data.instrumento2;
    document.getElementById('instrumento3').textContent = data.instrumento3;
    document.getElementById('objetivo').textContent = data.objetivo;
    document.getElementById('estilo').textContent = data.estilo;
    document.getElementById('telefone').textContent = data.telefone;
    document.getElementById('banda').textContent = data.bandaNome || 'Sem banda';
    // colocar banda no local storage

    if (data.profileImage) {
        const profileImageElem = document.getElementById('fotoPerfil');
        profileImageElem.src = `data:image/png;base64,${data.profileImage}`;
        profileImageElem.style.display = 'block';
    }
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('id');
    localStorage.removeItem('secret');
    localStorage.removeItem('usuario');
    window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
}

//funcao para apagar o usuario, ela vai apagar o usuario do banco de dados e do local storage, primeiramente, ela vai pedir a senha do usuario e depois vai pedir confirmacao
function excluirPerfil() {
    const senha = prompt('Digite sua senha para confirmar a exclusão do perfil:');
    if (!senha) {
        return;
    }
    const confirmacao = prompt('Tem certeza que deseja excluir seu perfil? Digite "SIM" para confirmar:');
    if (confirmacao !== 'SIM') {
        return;
    }
    const id = localStorage.getItem('id');
    const token = localStorage.getItem('token');
    fetch(`http://localhost:6789/usuario/delete?id=${id}&senha=${senha}`, {
        method: 'DELETE',
        headers: {
            
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Resposta do servidor:', data);
        alert('Perfil excluído com sucesso!');
        localStorage.removeItem('token');
        localStorage.removeItem('id');
        localStorage.removeItem('secret');
        localStorage.removeItem('usuario');
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
    })
    .catch(error => {
        console.error('Erro ao excluir o perfil:', error);
        alert('Senha incorreta. Tente novamente.');
    });
}

function formatCurrency(value) {
    return `R$ ${value.toFixed(2).replace('.', ',')}`;
}

const cacheDisplay = document.getElementById('cache');
cacheDisplay.textContent = formatCurrency(parseFloat(cacheDisplay.textContent.replace('R$', '').replace('.', '').replace(',', '.')));

function editarPerfil() {
    document.getElementById('instrumento1SELECT').style.display = 'block';
    //pré selecionar o instrumento1
    document.getElementById('instrumento1SELECT').value = localStorage.getItem('instrumento1');
    document.getElementById('instrumento2SELECT').style.display = 'block';
    document.getElementById('instrumento2SELECT').value = localStorage.getItem('instrumento2');
    document.getElementById('instrumento3SELECT').style.display = 'block';
    document.getElementById('instrumento3SELECT').value = localStorage.getItem('instrumento3');
    document.getElementById('descricao').contentEditable = true;
    document.getElementById('cache').contentEditable = true;
    document.getElementById('instrumento1').style.display = 'none';
    document.getElementById('instrumento2').style.display = 'none';
    document.getElementById('instrumento3').style.display = 'none';
    document.getElementById('objetivo').contentEditable = true;
    document.getElementById('estilo').contentEditable = true;
    document.getElementById('salvar').style.display = 'block';
    document.getElementById('editar').style.display = 'none';
    document.getElementById('fotoPerfil').style.display = 'block';
    document.getElementById('inputFotoPerfil').style.display = 'block';
    document.getElementById('telefone').contentEditable = true;
}

function salvar() {
    const nome = document.getElementById('nome').textContent.trim();
    const descricao = document.getElementById('descricao').textContent.trim();
    const cache = parseFloat(document.getElementById('cache').textContent.replace('R$', '').replace('.', '').replace(',', '.'));
    const instrumento1 = document.getElementById('instrumento1SELECT').value;
    const instrumento2 = document.getElementById('instrumento2SELECT').value;
    const instrumento3 = document.getElementById('instrumento3SELECT').value;
    const objetivo = document.getElementById('objetivo').textContent.trim();
    const estilo = document.getElementById('estilo').textContent.trim();
    const id = localStorage.getItem('id');
    const senha = localStorage.getItem('secret');
    const token = localStorage.getItem('token');
    const fotoPerfil = document.getElementById('inputFotoPerfil').files[0];
    const telefone = document.getElementById('telefone').textContent.trim();

    const formData = new FormData();
    formData.append('id', id);
    formData.append('nome', nome);
    formData.append('descricao', descricao);
    formData.append('senha', senha);
    formData.append('cache', cache);
    formData.append('instrumento1', instrumento1);
    formData.append('instrumento2', instrumento2);
    formData.append('instrumento3', instrumento3);
    formData.append('objetivo', objetivo);
    formData.append('estilo', estilo);
    formData.append('telefone', telefone);

    if (fotoPerfil) {
        formData.append('fotoPerfil', fotoPerfil);
    }

    fetch(`http://localhost:6789/usuario/update`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`
        },
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Resposta do servidor:', data);
        alert('Perfil atualizado com sucesso!');
        window.location.reload();
    })
    .catch(error => {
        console.error('Erro durante a requisição fetch:', error);
        alert('Erro ao atualizar o perfil. Tente novamente.');
    });
    document.getElementById('instrumento1SELECT').style.display = 'none';
    document.getElementById('instrumento2SELECT').style.display = 'none';
    document.getElementById('instrumento3SELECT').style.display = 'none';
    document.getElementById('instrumento1').style.display = 'block';
    document.getElementById('instrumento2').style.display = 'block';
    document.getElementById('instrumento3').style.display = 'block';
    document.getElementById('nome').contentEditable = false;
    document.getElementById('descricao').contentEditable = false;
    document.getElementById('cache').contentEditable = false;
    document.getElementById('objetivo').contentEditable = false;
    document.getElementById('estilo').contentEditable = false;
    document.getElementById('telefone').contentEditable = false;
    document.getElementById('editar').style.display = 'block';
    document.getElementById('salvar').style.display = 'none';
    document.getElementById('fotoPerfil').style.display = 'none';
    document.getElementById('inputFotoPerfil').style.display = 'none';

    // Atualizar o localStorage apenas após a leitura completa da imagem
    if (fotoPerfil) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const usuario = JSON.parse(localStorage.getItem('usuario'));
            usuario.nome = nome;
            usuario.descricao = descricao;
            usuario.cache = cache;
            usuario.instrumento1 = instrumento1;
            usuario.instrumento2 = instrumento2;
            usuario.instrumento3 = instrumento3;
            usuario.objetivo = objetivo;
            usuario.estilo = estilo;
            usuario.telefone = telefone;
            usuario.profileImage = e.target.result; // Armazenar a imagem como URL de dados
            localStorage.setItem('instrumento1', instrumento1);
            localStorage.setItem('instrumento2', instrumento2);
            localStorage.setItem('instrumento3', instrumento3);
            localStorage.setItem('usuario', JSON.stringify(usuario));
        }
        reader.readAsDataURL(fotoPerfil); // Inicia a leitura da imagem
    } else {
        // Se não houver nova foto, apenas atualize as outras informações do usuário no localStorage
        const usuario = JSON.parse(localStorage.getItem('usuario'));
        usuario.nome = nome;
        usuario.descricao = descricao;
        usuario.cache = cache;
        usuario.instrumento1 = instrumento1;
        usuario.instrumento2 = instrumento2;
        usuario.instrumento3 = instrumento3;
        usuario.objetivo = objetivo;
        usuario.estilo = estilo;
        usuario.telefone = telefone;
        //guardar todos os instrumentos no local storage
        localStorage.setItem('instrumento1', instrumento1);
        localStorage.setItem('instrumento2', instrumento2);
        localStorage.setItem('instrumento3', instrumento3);
        localStorage.setItem('usuario', JSON.stringify(usuario));
    }
}