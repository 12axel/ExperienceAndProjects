import { S3, PutObjectCommand } from '@aws-sdk/client-s3';
import { getSignedUrl } from '@aws-sdk/s3-request-presigner';

const s3 = new S3({
    region: 'us-east-2',
    credentials: {
        accessKeyId: 'AKIAS2VS4VJA3RMJ6OEV',
        secretAccessKey: '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',
    },
});

export const handler = async (event, context) => {
    context.callbackWaitsForEmptyEventLoop = false;

    let response = {
        headers: {
            "Access-Control-Allow-Headers": "Content-Type",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "POST"
        }
      };

    try {
        const { fileName, fileType } = event.body;

        const params = {
            Bucket: 'auctionhouse-images',
            Key: `${Date.now()}_${fileName}}`,
            ContentType: fileType,
            ACL: 'public-read',
        };

        const uploadURL = await getSignedUrl(s3, new PutObjectCommand(params), {
            expiresIn: 60,
        });


        response.statusCode = 200;
        response.body = JSON.stringify(uploadURL);

    } catch (error) {
        response.statusCode = 400;
        response.error = "Error generating signed URL";
    }

    return response;
};
