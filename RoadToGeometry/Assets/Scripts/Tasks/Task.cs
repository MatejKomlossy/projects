using System.Collections.Generic;
using System.Linq;
using UnityEngine;

namespace Tasks
{
    public class Task
    {
        private readonly List<GameObject> _objectPrefabs;

        private HashSet<string> _objectsToCollect = new();
        private int _objectsToCollectCount;
        private string _task;

        private Dictionary<string, List<string>> _tagsHints;  //<tag, hint>
        private const int MaxOneObjectCount = 4;
        private static System.Random _random;
        private const int PointsPerObject = 10;

        public int Points { get; private set; }
        
        public Task(List<GameObject> objectPrefabs, Dictionary<string, List<string>> tagsHints)
        {
            _random = new System.Random();
            _objectPrefabs = objectPrefabs;
            _tagsHints = tagsHints;
            FillObjectsToCollect();
            ComputePoints();
        }

        private void FillObjectsToCollect()
        {
            List<string> hints = _tagsHints.Values.SelectMany(x => x).ToHashSet().ToList();

            var index = _random.Next(hints.Count);
            _task = hints[index];
            _objectsToCollectCount = RandomObjectCount();
            foreach (string tag in _tagsHints.Keys)
            {
                foreach (string hint in _tagsHints[tag])
                {
                    if (hint.Trim().Equals(_task.Trim()))
                    {
                        _objectsToCollect.Add(tag);
                    }
                }
            }
        }

        private int RandomObjectCount()
        {
            return  _random.Next(MaxOneObjectCount) + 1;
        }

        public void Collect(GameObject collectible)
        {
            var tag = collectible.tag;
            if (_objectsToCollect.Contains(tag) && _objectsToCollectCount > 0)
            {
                _objectsToCollectCount -= 1;
            }

            if (IsCompleted())
            {
                EventManager.Instance.TaskCompleted(this);
            }
        }

        public bool IsObjectFromTheTask(GameObject collectible)
        {
            var tag = collectible.tag;
            if (_objectsToCollect.Contains(tag) && _objectsToCollectCount > 0)
            {
                return true;
            }
            return false;
        }

        private bool IsCompleted()
        {
            return _objectsToCollectCount <= 0;
        }

        private void ComputePoints()
        {
           Points += _objectsToCollectCount * PointsPerObject;
        }

        public string TaskStrings()
        {
            return _objectsToCollectCount + "x " + _task;
        }
    }
}