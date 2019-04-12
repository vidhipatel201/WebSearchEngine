var app = angular.module("myApp",[]);

app.controller("searchController", function($scope,$http) {
	
	$scope.search = function(){
		console.log($scope.inputKeyword);
		
		$http({
			method : 'POST',
			url : 'acc/test/'+$scope.inputKeyword,
			headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).then(function(response) {
			$scope.result = response.data;
			$scope.flag = true;
			
			$scope.suggestedWord = $scope.result[0];
			if($scope.suggestedWord === ""){
				$scope.flag = false;
			}
			console.log($scope.result[0]);
			$scope.result.shift();
			$scope.flag1 = false;
			if($scope.result.length === 0){
				$scope.flag1 = true;
			}
			$scope.count = $scope.result.length;
			console.log($scope.result);
			//console.log($scope.finalResult);
		}, function(error) {
			alert("Failed to get data, status=" + error);
		});
		
	}

	
	
});