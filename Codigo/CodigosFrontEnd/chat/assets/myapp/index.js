const fs = require('fs');
const msRest = require('@azure/ms-rest-js');
const PredictionApi = require('@azure/cognitiveservices-customvision-prediction');

// Retrieve environment variables
const predictionKey = '7a74e928ed2a4dcb87a8676ca0f2a0db';
const predictionEndpoint = 'https://ti2reconhecimentofacial.cognitiveservices.azure.com/customvision/v3.0/Prediction/7faff063-f449-4349-8ffc-f99442a067cb/detect/iterations/Iteration6/image';
const projectId = '7faff063-f449-4349-8ffc-f99442a067cb'; // Substitua pelo ID do seu projeto
const publishIterationName = "Iteration6";  // Substitua pelo nome correto da sua iteração publicada

// Configurar o cliente da API de predição
const credentials = new msRest.ApiKeyCredentials({ inHeader: { 'Prediction-key': predictionKey } });
const predictor = new PredictionApi.PredictionAPIClient(credentials, predictionEndpoint);

async function classifyImageFromFile(imagePath) {
    try {
        // Leia o arquivo de imagem
        const imageBuffer = fs.readFileSync(imagePath);

        // Faça a predição usando o arquivo de imagem
        const results = await predictor.classifyImage(projectId, publishIterationName, imageBuffer);

        // Mostrar os resultados
        console.log("Results:");
        results.predictions.forEach(predictedResult => {
            console.log(`\t${predictedResult.tagName}: ${(predictedResult.probability * 100.0).toFixed(2)}%`);
        });
    } catch (error) {
        console.error("Error classifying image:", error);
    }
}

// Caminho para o arquivo de imagem a ser classificada
const imagePath = "master-1.jpg";

// Classificar a imagem
classifyImageFromFile(imagePath);
