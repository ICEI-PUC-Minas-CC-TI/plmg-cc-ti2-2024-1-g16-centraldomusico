document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    const nomeBanda = params.get('nomeBanda');
    const joinButton = document.getElementById('join-band-button');
    const leaveButton = document.getElementById('leave-band-button');
    const deleteButton = document.getElementById('delete-band-button');
    const feedbackMessage = document.getElementById('feedback-message');
    const userId = localStorage.getItem('id'); // Pegando o ID do usuário armazenado no localStorage
    const token = localStorage.getItem('token');

    if (!token) {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
        return;
    }else{
        document.getElementById('loginbotao').style.display = 'none';
    }

    fetch(`http://localhost:6789/banda/getByName?nomeBanda=${nomeBanda}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
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
        const bandaId = data.id;
        const container = document.getElementById('banda-container');
        const bandaDetails = `
            <h1>${data.nomeBanda}</h1>
            <p class="desc">${data.descricao}</p>
            <p class= "cache">Cache: R$ ${data.cache}</p>
            <p class= "datacriacao">Data de Criação: ${data.dataCriacao}</p>
            <p class="obj">Objetivo: ${data.objetivo}</p>
            <p class="estilo">Estilo: ${data.estilo}</p>
        `;
        container.innerHTML = bandaDetails;

        joinButton.addEventListener('click', function() {
            fetch('http://localhost:6789/banda/join', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ bandaId: data.id, musicoId: userId })
            })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text); });
                }
                return response.json();
            })
            .then(data => {
                feedbackMessage.textContent = data.message;
                feedbackMessage.style.color = 'green';
                fetchMembers(data.id); // Refresh members list
                //adicione o id da banda no localstorage
                console.log('DATA: ',data);
                const usuario = JSON.parse(localStorage.getItem('usuario'));
                //setar o atributo bandaNome
                usuario.bandaNome = nomeBanda;
                console.log('BANDA DO USUARIO: ',usuario.bandaNome);

                //salvar objeto no local storage
                localStorage.setItem('usuario', JSON.stringify(usuario));
                localStorage.setItem('bandaId', bandaId);
                localStorage.setItem('bandaNomeUser', nomeBanda);
                //f5
                location.reload(true);
                //colocar alert de bem vindo com nome da banda
                alert('Bem vindo(a) a banda: ' + nomeBanda);
                //atualizar o localstorage

            })
            .catch(error => {
                console.error('Erro ao entrar na banda:', error);
                if (error.message === 'Você já está nesta banda.') {
                    alert('Você já está em uma banda!');
                } else {
                    feedbackMessage.textContent = 'Erro ao entrar na banda. Tente novamente.';
                    feedbackMessage.style.color = 'red';
                }
            });
        });

        leaveButton.addEventListener('click', function() {

            var idBanda = parseInt(localStorage.getItem('bandaId'));
            console.log(typeof idBanda);
            console.log(typeof bandaId);
            if (idBanda !== bandaId) {
                alert('Você não está nesta banda.');
                return;
            }

            if (!confirm('Tem certeza que deseja sair da banda?')) {
                return;
            }
            fetch('http://localhost:6789/banda/leave', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ bandaId: data.id, musicoId: userId })
            })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text); });
                }
                return response.json();
            })
            .then(data => {
                feedbackMessage.textContent = data.message;
                feedbackMessage.style.color = 'green';
                //limpar o localstorage
                localStorage.removeItem('bandaId');
                //atualizar o localstorage
                const usuario = JSON.parse(localStorage.getItem('usuario'));
                console.log('BANDA DO USUARIO: ',usuario.bandaNome);
                //setar o atributo bandaNome
                usuario.bandaNome = 'Sem banda';
                //salvar objeto no local storage
                localStorage.setItem('usuario', JSON.stringify(usuario));
                fetchMembers(data.id); // Refresh members list 
                //f5
                location.reload(true);
            })
            .catch(error => {
                console.error('Erro ao sair da banda:', error);
                feedbackMessage.textContent = 'Erro ao sair da banda. Tente novamente.';
                feedbackMessage.style.color = 'red';
            });
        });
        deleteButton.addEventListener('click', function() {
            var idBanda = parseInt(localStorage.getItem('bandaId'));
            if (idBanda !== bandaId) {
                alert('Você não está nesta banda.');
                return;
            }
            deleteBand();
        });
        fetchMembers(data.id);
    })
    .catch(error => {
        console.error('Erro ao buscar dados da banda:', error);
        const errorMsg = document.createElement('p');
        errorMsg.textContent = 'Erro ao carregar os detalhes da banda. Tente novamente.';
        document.getElementById('banda-container').appendChild(errorMsg);
    });

    function fetchMembers(bandaId) {
        fetch(`http://localhost:6789/banda/members?bandaId=${bandaId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro na requisição: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            const membersContainer = document.getElementById('members-container');
            membersContainer.innerHTML = ''; // Limpa o conteúdo atual do container de membros
    
            data.forEach(member => {
                const memberElement = document.createElement('div');
                memberElement.className = 'member';
    
                let profileImageUrl = '/Codigo/CodigosFrontEnd/perfil/assets/css/TemplateFotoPerfil.png'; // Caminho para a imagem padrão
    
                if (member.profileImage && member.profileImage.length > 0) {
                    const byteArray = new Uint8Array(member.profileImage);
                    const blob = new Blob([byteArray], { type: 'image/jpeg' }); // Ajuste o tipo conforme necessário
                    profileImageUrl = URL.createObjectURL(blob);
                }
                //printar membro
                console.log('MEMBRO: ',member);
                // Constrói o HTML do membro, incluindo a imagem de perfil e envolvendo em uma ancora que leva ao perfil do músico
                memberElement.innerHTML = `
                    <div class="member-info">
                        <a href="/Codigo/CodigosFrontEnd/perfil/perfilExterno.html?id=${member.id}&instrumento1=${member.instrumento1}&instrumento2=${member.instrumento2}&instrumento3=${member.instrumento3}" style="text-decoration: none; color: inherit;">
                        <img src="${profileImageUrl}" alt="Foto de Perfil">
                        <div>
                            <p>Nome: ${member.nome}</p>
                            <p>Instrumentos: 
                                ${member.instrumento1 || ''} 
                                ${member.instrumento2 ? ', ' + member.instrumento2 : ''} 
                                ${member.instrumento3 ? ', ' + member.instrumento3 : ''}
                            </p>
                        </div>
                        </a>
                    </div>
                `;
                
                membersContainer.appendChild(memberElement); // Adiciona o elemento de membro ao container
            });
        })
        .catch(error => {
            console.error('Erro ao buscar membros da banda:', error);
            const errorMsg = document.createElement('p');
            errorMsg.textContent = 'Erro ao carregar os membros da banda. Tente novamente.';
            document.getElementById('members-container').appendChild(errorMsg);
        });
    }
});

//funcao para deletar banda, vai pedir a senha da banda, se a senha estiver correta, perguntará ao usuario se ele tem certeza que deseja deletar a banda
//a funcao mandará na requisicao o id da banda e a senha da banda
function deleteBand() {
    if (!confirm('Tem certeza que deseja deletar a banda?')) {
        return;
    }    
    const senha = prompt('Digite a senha da banda para confirmar a exclusão:');

    if (!senha) {
        return;
    }

    const bandaId = localStorage.getItem('bandaId');
    const userId = localStorage.getItem('id');

    fetch('http://localhost:6789/banda/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ bandaId, musicoId: userId, senha })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.json();
    })
    .then(data => {
        alert(data.message);
        localStorage.removeItem('bandaId');
        //atualizar o localstorage
        const usuario = JSON.parse(localStorage.getItem('usuario'));
        console.log('BANDA DO USUARIO: ',usuario.bandaNome);
        //setar o atributo bandaNome
        usuario.bandaNome = 'Sem banda';
        //salvar objeto no local storage
        localStorage.setItem('usuario', JSON.stringify(usuario));
        window.location.href = '/Codigo/CodigosFrontEnd/bandas/bandas.html';
    })
    .catch(error => {
        console.error('Erro ao deletar banda:', error);
        alert('Erro ao deletar banda. Tente novamente.');
    });
}