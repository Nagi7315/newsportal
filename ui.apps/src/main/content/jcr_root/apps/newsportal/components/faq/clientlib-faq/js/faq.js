document.addEventListener("DOMContentLoaded", () => {

    // Get the hash part from the URL
    const hash = window.location.hash;

    // Check if it exists and is not just empty '#'
    if (hash && hash.length > 1) {
        // Call your custom logic with the hash
        openAccordionById(hash);
    }
    const headers = document.querySelectorAll(".accordion-header");

    //accordion toggling behaviour function
    function toggleAccordion(content, open) {
        const header = content?.previousElementSibling;
        const icon = header?.querySelector(".accordion-icon");

        content?.classList.toggle("open", open);
        header?.classList.toggle("active", open);
        content.style.maxHeight = open ? `${content.scrollHeight}px` : null;
        if (icon) icon.textContent = open ? "−" : "+";
    }

    function closeAllAccordions(exclude = null) {
        document.querySelectorAll(".accordion-content.open").forEach(openContent => {
            if (openContent !== exclude) toggleAccordion(openContent, false);
        });
    }

    //Add click event listener to each accordion header
    headers.forEach(header => {
        header.addEventListener("click", () => {
            const content = header.nextElementSibling;
            const isOpen = content.classList.contains("open");

            closeAllAccordions(content);
            toggleAccordion(content, !isOpen);
        });
    });

    //Fuction that open accordion by it ID
    function openAccordionById(id) {
        const target = document.getElementById(id.replace("#", ""));
        if (!target?.classList.contains("accordion-container")) return;

        const content = target.querySelector(".accordion-content");
        closeAllAccordions(content);
        toggleAccordion(content, true);

        setTimeout(() => {
            target.scrollIntoView({ behavior: "smooth", block: "start" });
        }, 300);
    }

    document.addEventListener("click", e => {
        // look up from whatever was clicked to the nearest <a href="#...">
        const a = e.target.closest('a[href^="#"]:not([href="#"]):not([href^="#/"])');
        if (!a) return;                   // not one of our special links
        e.preventDefault();
        console.log("Anchor clicked:", a.getAttribute("href"));
        openAccordionById(a.getAttribute("href"));
    });
});
