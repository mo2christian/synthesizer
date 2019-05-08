# synthesizer

Application permettant de convertir le texte en voie.

Déployé sur AWS et accessible à l'adresse https://just2teach.com.

Les composants utilisés sont :
 - Certificate managerpour generer le certificat SSL
 - Cloudfront comme CDN afin d'accelerer le chargement des pages
 - S3 pour le stockage des fichiers statiques
 - API Point d'entrée de notre backend
 - Lambda convertie le texte en voix et le stockque dans S3
 - Polly service AWS permettant de convertir le texte en voix
