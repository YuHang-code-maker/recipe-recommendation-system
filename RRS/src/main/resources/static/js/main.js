//TO DO
(()=>{
	'use strict';
	const ROOT_URL = 'http://localhost:8085/api/recipes';
	const findAll = ()=>{
		$.ajax({
			type:'GET',
			url: ROOT_URL,
			dataType: 'json',
			success:renderList
		});
	};
	
	const renderList = (recipes)=>{
		$('#recipeList').empty();
		recipes.forEach(recipe=>{
			const html=`
				<div class="col-md-6 col-lg-4 mb-4">
			          <div class="card recipe-card h-100 shadow-sm">
			                  <img src="/images/${recipe.image}" class="card-img-top recipe-img">
			                   <div class="card-body text-center">
			                        <h5 class="card-title">${recipe.title}</h5>
			                        <button class="btn btn-warning infoButton" data-id="${recipe.id}">More Info</button>
			                   </div>
			          </div>
			    </div>	
			`;
			$('#recipeList').append(html);
		});
	};
	
	$(()=>{
		findAll();
	})
	
	
})();