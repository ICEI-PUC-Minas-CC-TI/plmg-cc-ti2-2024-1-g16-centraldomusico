document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    const nomeBanda = params.get('nomeBanda');
    const joinButton = document.getElementById('join-band-button');
    const leaveButton = document.getElementById('leave-band-button');
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
                localStorage.setItem('bandaId', bandaId);
                //f5
                location.reload(true);
                //colocar alert de bem vindo com nome da banda
                alert('Bem vindo(a) a banda: ' + nomeBanda);
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
            //popup pedindo para o usuario confirmar a saida da banda
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
            membersContainer.innerHTML = '';
            data.forEach(member => {
                const memberElement = document.createElement('div');
                memberElement.className = 'member';
                memberElement.innerHTML = `
                    Nome: ${member.nome}<br>
                    Instrumentos: 
                    ${member.instrumento1 || ''} 
                    ${member.instrumento2 ? ', ' + member.instrumento2 : ''} 
                    ${member.instrumento3 ? ', ' + member.instrumento3 : ''}
                `;
                membersContainer.appendChild(memberElement);
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
