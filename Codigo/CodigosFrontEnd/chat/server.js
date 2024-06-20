const msRest = require('@azure/ms-rest-js');
const PredictionApi = require('@azure/cognitiveservices-customvision-prediction');

// Configuração dos dados de predição
const predictionKey = '7a74e928ed2a4dcb87a8676ca0f2a0db';
const predictionEndpoint = 'https://ti2reconhecimentofacial.cognitiveservices.azure.com/customvision/v3.0/Prediction/7faff063-f449-4349-8ffc-f99442a067cb/detect/iterations/Iteration6/image';
const projectId = '7faff063-f449-4349-8ffc-f99442a067cb'; // Substituir pelo ID do seu projeto
const publishIterationName = "Iteration6";  // Substituir pelo nome correto da iteração publicada

// Configurar o cliente da API de predição
const credentials = new msRest.ApiKeyCredentials({ inHeader: { 'Prediction-key': predictionKey } });
const predictor = new PredictionApi.PredictionAPIClient(credentials, predictionEndpoint);

// Função para classificar a imagem a partir de um objeto File
async function classifyImageFromFile(file) {
    try {
        // Verificar se um arquivo foi selecionado
        if (!file) {
            console.error("Nenhum arquivo selecionado.");
            return;
        }

        // Ler o conteúdo do arquivo como ArrayBuffer
        const reader = new FileReader();
        reader.onload = async function() {
            try {
                // Obter o ArrayBuffer do arquivo
                const imageArrayBuffer = reader.result;

                // Fazer a predição usando o ArrayBuffer
                const results = await predictor.classifyImage(projectId, publishIterationName, imageArrayBuffer);

                // Mostrar os resultados
                console.log("Resultados:");
                results.predictions.forEach(predictedResult => {
                    console.log(`\t${predictedResult.tagName}: ${(predictedResult.probability * 100.0).toFixed(2)}%`);
                });
            } catch (error) {
                console.error("Erro ao classificar imagem:", error);
            }
        };
        reader.readAsArrayBuffer(file);
    } catch (error) {
        console.error("Erro ao ler arquivo:", error);
    }
}
