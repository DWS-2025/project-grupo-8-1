let currentPage = 1;
const reviewsPerPage = 5;

async function loadReviews(page) {
    if (page < 1) return;

    try {
        const response = await fetch(`/api/review/paginated?page=${page}&size=${reviewsPerPage}`);
        if (!response.ok) throw new Error("Error loading reviews");

        const data = await response.json();

        // Validate the structure of the response
        if (!Array.isArray(data.reviews)) {
            throw new Error("The response structure is not valid");
        }

        const reviewsContainer = document.getElementById("reviews-container");
        reviewsContainer.innerHTML = "";

        data.reviews.forEach(review => {
            const reviewElement = document.createElement("ul");
            reviewElement.innerHTML = `
                <li>User: ${review.user}</li>
                <li>Date: ${review.date}</li>
                <li>Review: ${review.content}</li>
                <p>
                <button type="button" onclick="edit('${review.id}')">edit</button>
                <button type="button" onclick="del('${review.id}')">delete</button>
                </p>
                <br>
            `;
            reviewsContainer.appendChild(reviewElement);
        });

        currentPage = page;
        document.getElementById("prev-page").disabled = page === 1;
        document.getElementById("next-page").disabled = !data.hasMore;
    } catch (error) {
        console.error("Error:", error.message);
    }
}

// Load the first page on startup
document.addEventListener("DOMContentLoaded", () => loadReviews(currentPage));
