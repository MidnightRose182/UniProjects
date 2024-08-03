#Python code for processing the Knapsack Problem. Can be expanded to incorporate ML algorithms and techniques to find optimal solutions.

from itertools import permutations
import time

class Item:
    def __init__(self, Weight, Value) -> None:
        self.weight = Weight
        self.value = Value
    def __repr__(self) -> str:
        return f"({self.weight}, {self.value})"
    
class Box:
    def __init__(self, Capacity) -> None:
        self.items =[]
        self.capacity = Capacity
        self.contents = 0
    
    def empty(self) -> None:
        self.contents = 0
        self.items = []
    
    def numItems(self) -> int:
        return len(self.items)
    
    def __repr__(self) -> str:
        return "[{}, {}/{}]".format(self.numItems(), self.contents, self.capacity)
    
    def fits(self, Item) -> bool:
        return self.contents + Item.weight <= self.capacity
    
    def totalVal(self) -> int:
        val = 0
        for item in self.items:
            val += item.value
        return val

    
    def put(self, Item) -> None:
        if self.fits(Item):
            self.items.append(Item)
            self.contents += Item.weight
        else:
            raise ValueError ("Capacity Exceeded")
    
ItemWeightAndVals = [(5,20), (5,100), (10, 10),(5, 25), (15, 50), (10, 200)]
BigBox = Box(25)

Items = [Item(n[0], n[1]) for n in ItemWeightAndVals]
NumItems = len(Items)

ItemPermutations = list(permutations(Items))
Evaluations = 0
HighestVal = 0

def fill_box(ItemList):
    global Evaluations
    global HighestVal
    ItemIndex = 0
    MaxValue = 0

    while ItemIndex < NumItems:
        CurrentItem = ItemList[ItemIndex]
        if BigBox.fits(CurrentItem):
            BigBox.put(CurrentItem)
            MaxValue += CurrentItem.value
            ItemIndex += 1
        Evaluations += 1
    
    if BigBox.totalVal > HighestVal:
        HighestVal = BigBox.totalVal


def main():
    for Iteration, Permutation in enumerate(ItemPermutations):
        fill_box(Permutation)
    print(HighestVal)

if __name__ == "__main__": main()